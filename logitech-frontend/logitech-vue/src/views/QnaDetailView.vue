<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAccountStore } from '@/store/accountStore'
import { getQnaDetail, addAnswer, updateQna, deleteQna } from '@/services/qnaService'

const route = useRoute()
const router = useRouter()
const accountStore = useAccountStore()

const question = ref(null)   // level=1 질문
const answer = ref(null)     // level=2 답변
const showAnswerModal = ref(false)
const showEditModal = ref(false)
const answerForm = ref({ content: '' })
const editForm = ref({ title: '', content: '' })
const editingQnaId = ref(null)

const load = async () => {
    const res = await getQnaDetail(route.params.id)
    if (res?.status === 200) {
        question.value = res.data.find(q => q.level === 1)
        answer.value = res.data.find(q => q.level === 2) || null
    }
}

onMounted(() => load())

const submitAnswer = async () => {
    if (!answerForm.value.content.trim()) {
        alert('답변 내용을 입력해주세요.')
        return
    }
    const res = await addAnswer(question.value.id, {
        title: '답변',
        content: answerForm.value.content,
        writer: accountStore.loginUser.name
    })
    if (res?.status === 200) {
        alert('답변이 등록되었습니다.')
        showAnswerModal.value = false
        await load()
    }
}

// 수정 모달 열기
const openEditModal = (target) => {
    editingQnaId.value = target.id
    editForm.value = { title: target.title, content: target.content }
    showEditModal.value = true
}

// 수정 제출
const submitEdit = async (id) => {
    const res = await updateQna(id, editForm.value)
    if (res?.status === 200) {
        alert('수정되었습니다.')
        showEditModal.value = false
        await load()
    }
}

// 삭제
const remove = async (id) => {
    if (!confirm('삭제하시겠습니까?')) return
    const res = await deleteQna(id)
    if (res?.status === 200 || res?.status === 204) {
        alert('삭제되었습니다.')
        router.push('/qna')
    } else {
        alert('삭제 중 오류가 발생했습니다.')
    }
}

const formatDate = (dateStr) => {
    if (!dateStr) return '-'
    return new Date(dateStr).toLocaleString('ko-KR')
}

const isMyPost = (writer) => accountStore.loginUser?.name === writer
const isAdmin = () => accountStore.loginUser?.role === 'ADMIN'
</script>

<template>
  <div class="container py-5">
    <div v-if="question">

      <!-- 질문 -->
      <div class="card shadow-sm mb-4">
        <div class="card-header bg-white d-flex justify-content-between align-items-center">
          <span class="badge bg-primary me-2">질문</span>
          <span class="fw-bold fs-5">{{ question.title }}</span>
          <small class="text-muted ms-auto">{{ question.writer }} | {{ formatDate(question.createdAt) }} | 조회 {{ question.viewCount }}</small>
        </div>
        <div class="card-body">
          <p style="white-space: pre-wrap">{{ question.content }}</p>
        </div>
        <div class="card-footer bg-white d-flex gap-2 justify-content-end">
          <button v-if="isAdmin() && !answer"
            class="btn btn-sm btn-success" @click="showAnswerModal = true">
            <i class="bi bi-chat-dots me-1"></i>답변 등록
          </button>
          <button v-if="isMyPost(question.writer) || isAdmin()"
            class="btn btn-sm btn-outline-warning" @click="openEditModal(question)">
            수정
          </button>
          <button v-if="isMyPost(question.writer) || isAdmin()"
            class="btn btn-sm btn-danger" @click="remove(question.id)">
            삭제
          </button>
        </div>
      </div>

      <!-- 답변 -->
      <div v-if="answer" class="card shadow-sm mb-4 border-success">
        <div class="card-header bg-success text-white d-flex justify-content-between">
          <span>✅ 답변</span>
          <small>{{ answer.writer }} | {{ formatDate(answer.createdAt) }}</small>
        </div>
        <div class="card-body">
          <p style="white-space: pre-wrap">{{ answer.content }}</p>
        </div>
        <div class="card-footer bg-white d-flex gap-2 justify-content-end">
          <button v-if="isAdmin()"
            class="btn btn-sm btn-outline-warning" @click="openEditModal(answer)">
            답변 수정
          </button>
          <button v-if="isAdmin()"
            class="btn btn-sm btn-danger" @click="remove(answer.id)">
            답변 삭제
          </button>
        </div>
      </div>

      <div v-else class="text-center text-muted py-3">
        아직 답변이 없습니다.
      </div>

    </div>

    <div v-else class="text-center py-5">
      <div class="spinner-border text-primary"></div>
    </div>

    <button class="btn btn-outline-secondary mt-3" @click="router.push('/qna')">
      <i class="bi bi-arrow-left me-1"></i>목록으로
    </button>

    <!-- 답변 등록 모달 -->
    <div v-if="showAnswerModal" class="modal-backdrop">
      <div class="modal-box card shadow p-4">
        <h5 class="mb-3">답변 등록</h5>
        <div class="mb-4">
          <label class="form-label">답변 내용</label>
          <textarea v-model="answerForm.content" class="form-control" rows="5"
            placeholder="답변을 입력해주세요"></textarea>
        </div>
        <div class="d-flex gap-2">
          <button class="btn btn-success w-100" @click="submitAnswer">등록</button>
          <button class="btn btn-outline-secondary w-100" @click="showAnswerModal = false">취소</button>
        </div>
      </div>
    </div>

    <!-- 수정 모달 -->
    <div v-if="showEditModal" class="modal-backdrop">
      <div class="modal-box card shadow p-4">
        <h5 class="mb-3">수정</h5>
        <div class="mb-3">
          <label class="form-label">제목</label>
          <input v-model="editForm.title" type="text" class="form-control" />
        </div>
        <div class="mb-4">
          <label class="form-label">내용</label>
          <textarea v-model="editForm.content" class="form-control" rows="5"></textarea>
        </div>
        <div class="d-flex gap-2">
          <button class="btn btn-primary w-100"
            @click="submitEdit(editingQnaId)">
            수정 완료
          </button>
          <button class="btn btn-outline-secondary w-100" @click="showEditModal = false">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}
.modal-box {
  width: 600px;
  background: white;
  border-radius: 8px;
}
</style>

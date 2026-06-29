<script setup>
import { ref, onMounted } from "vue";
import { getItems, saveItem, updateItem, deleteItem } from "@/services/itemService";
import StockManagement from '@/components/StockManagement.vue'
import { resolveMediaUrl } from "@/utils/mediaUrl";

const items = ref([]);
const showModal = ref(false);
const editTarget = ref(null);

const form = ref({ itemName: "", imgPath: "", price: 0, stock: 0, category: "NEW" });

onMounted(() => loadItems());

const loadItems = async () => {
  const res = await getItems();
  if (res?.status === 200) items.value = res.data;
};

const openModal = (item = null) => {
  editTarget.value = item;
  form.value = item
    ? { itemName: item.itemName, imgPath: item.imgPath, price: item.price }
    : { itemName: "", imgPath: "", price: 0 };
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  editTarget.value = null;
};

const submitForm = async () => {
  if (!form.value.itemName || !form.value.imgPath || !form.value.price) {
    alert("모든 항목을 입력해주세요.");
    return;
  }

  let res;
  if (editTarget.value) {
    res = await updateItem(editTarget.value.itemId, form.value);
  } else {
    res = await saveItem(form.value);
  }

  if (res?.status === 200 || res?.status === 201) {
    await loadItems();
    closeModal();
  } else {
    alert("처리 중 오류가 발생했습니다.");
  }
};

const removeItem = async (itemId, itemName) => {
  if (!confirm(`'${itemName}' 상품을 삭제하시겠습니까?`)) return;
  const res = await deleteItem(itemId);
  if (res?.status === 200 || res?.status === 204) {
    await loadItems();
  } else {
    alert("삭제 중 오류가 발생했습니다.");
  }
};

const formatDate = (dateStr) => {
  if (!dateStr) return "-";
  return new Date(dateStr).toLocaleDateString("ko-KR");
};
</script>

<template>
  <div class="container-fluid py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h3>상품 관리</h3>
      <button class="btn btn-primary" @click="openModal()">
        <i class="bi bi-plus-lg me-1"></i>상품 등록
      </button>
    </div>

    <!-- 상품 목록 -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-hover mb-0">
          <thead class="table-dark">
            <tr>
              <th>ID</th>
              <th>이미지</th>
              <th>상품명</th>
              <th>가격</th>
              <th>등록일</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="items.length === 0">
              <td colspan="6" class="text-center py-4 text-muted">상품이 없습니다.</td>
            </tr>
            <tr v-for="item in items" :key="item.itemId">
              <td>{{ item.itemId }}</td>
              <td>
                <img
                  :src="resolveMediaUrl(item.imgPath)"
                  style="width: 60px; height: 60px; object-fit: cover"
                  class="rounded"
                  @error="(e) => (e.target.style.display = 'none')"
                />
              </td>
              <td>{{ item.itemName }}</td>
              <td>{{ item.price.toLocaleString() }}원</td>
              <td>{{ formatDate(item.createdAt) }}</td>
              <td>
                <div class="d-flex gap-1">
                  <button class="btn btn-sm btn-outline-warning" @click="openModal(item)">
                    수정
                  </button>
                  <button
                    class="btn btn-sm btn-danger"
                    @click="removeItem(item.itemId, item.itemName)"
                  >
                    삭제
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 등록/수정 모달 -->
    <div v-if="showModal" class="modal-backdrop">
  <div class="modal-box card shadow p-4">
    <h5 class="mb-3">{{ editTarget ? "상품 수정" : "상품 등록" }}</h5>

    <div class="mb-3">
      <label class="form-label">상품명</label>
      <input v-model="form.itemName" type="text" class="form-control" />
    </div>
    <div class="mb-3">
      <label class="form-label">이미지 URL</label>
      <input v-model="form.imgPath" type="text" class="form-control" placeholder="https://..." />
      <div v-if="form.imgPath" class="mt-2">
        <img :src="resolveMediaUrl(form.imgPath)" style="height: 100px; object-fit: cover" class="rounded"
          @error="(e) => (e.target.style.display = 'none')" />
      </div>
    </div>
    <div class="mb-3">
      <label class="form-label">가격</label>
      <input v-model.number="form.price" type="number" class="form-control" />
    </div>

    <!-- 여기 추가 -->
    <div class="mb-3">
      <label class="form-label">재고 수량</label>
      <input v-model.number="form.stock" type="number" class="form-control" />
    </div>
    <div class="mb-4">
      <label class="form-label">카테고리</label>
      <select v-model="form.category" class="form-select">
        <option value="NEW">신상품</option>
        <option value="BEST">베스트</option>
        <option value="RECOMMEND">추천상품</option>
      </select>
    </div>
    <div class="d-flex gap-2">
      <button class="btn btn-primary w-100" @click="submitForm">
        {{ editTarget ? "수정 완료" : "등록" }}
      </button>
      <button class="btn btn-outline-secondary w-100" @click="closeModal">취소</button>
    </div>
  </div>
</div>

    
    <h4 class="mt-5 mb-3">재고 관리</h4>
    <StockManagement />

  </div>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}
.modal-box {
  width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  background: white;
  border-radius: 8px;
}
</style>

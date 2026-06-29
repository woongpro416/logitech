<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAccountStore } from '@/store/accountStore'
import { useCartStore } from '@/store/cartStore'
import { getItem } from '@/services/itemService'
import { addItem } from '@/services/cartService'
import { getReviewsByItem } from '@/services/reviewService'
import { resolveMediaUrl } from '@/utils/mediaUrl'

const route = useRoute()
const router = useRouter()
const accountStore = useAccountStore()
const cartStore = useCartStore()
const item = ref(null)
const reviews = ref([])

onMounted(async () => {
  const res = await getItem(route.params.itemId)
  if (res?.status === 200) item.value = res.data

  const reviewRes = await getReviewsByItem(route.params.itemId)
  if (reviewRes?.status === 200) reviews.value = reviewRes.data
})

const addToCart = async () => {
  if (!accountStore.loggedIn) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }
  const res = await addItem(item.value.itemId)
  if (res?.status === 200 && confirm('장바구니에 담았습니다. 장바구니로 이동하시겠습니까?')) {
    router.push('/carts')
  }
}

const goOrder = () => {
  if (!accountStore.loggedIn) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }
  cartStore.setOrderItems([{
    cartId: null,
    item: {
      itemId: item.value.itemId,
      itemName: item.value.itemName,
      price: item.value.price
    }
  }])
  router.push('/orders')
}

const formatDate = (dateStr) => { 
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('ko-KR')
}

const renderStars = (rating) => '★'.repeat(rating) + '☆'.repeat(5 - rating)
</script>


<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div v-if="item" class="card shadow-sm">
          <div class="item-img"
            :style="{ backgroundImage: `url(${resolveMediaUrl(item.imgPath)})` }">
          </div>
          <div class="card-body p-4">
            <h4 class="card-title">{{ item.itemName }}</h4>
            <p class="text-muted">등록일: {{ formatDate(item.createdAt) }}</p>
            <hr>
            <div class="d-flex justify-content-between align-items-center mt-3">
              <span class="fs-4 fw-bold text-primary">{{ item.price.toLocaleString() }}원</span>
              <div class="d-flex gap-2">
                <button class="btn btn-outline-primary" @click="addToCart">
                  <i class="bi bi-cart-plus me-1"></i>장바구니
                </button>
                <button class="btn btn-primary" @click="goOrder">
                  <i class="bi bi-bag-check me-1"></i>바로 주문
                </button>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center py-5">
          <div class="spinner-border text-primary"></div>
        </div>

        <!-- 리뷰 섹션 -->
        <div class="mt-4">
          <h5 class="section-title mb-3">리뷰 ({{ reviews.length }})</h5>
          <div v-if="reviews.length === 0"
            class="text-center text-muted py-4 border rounded">
            아직 작성된 리뷰가 없습니다.
          </div>
          <div v-for="review in reviews" :key="review.reviewId"
            class="card mb-3 shadow-sm">
            <div class="card-body">
              <div class="d-flex justify-content-between align-items-center mb-2">
                <div>
                  <span class="fw-bold me-2">{{ review.memberName }}</span>
                  <span class="text-warning">{{ renderStars(review.rating) }}</span>
                </div>
                <small class="text-muted">{{ formatDate(review.createdAt) }}</small>
              </div>
              <p class="mb-0">{{ review.content }}</p>
            </div>
          </div>
        </div>

        <div class="mt-3">
          <button class="btn btn-outline-secondary" @click="router.back()">
            <i class="bi bi-arrow-left me-1"></i>목록으로
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.item-img {
  width: 100%;
  height: 400px;
  background-size: cover;
  background-position: center;
}
.section-title {
  font-weight: bold;
  border-left: 4px solid #0d6efd;
  padding-left: 10px;
}
</style>

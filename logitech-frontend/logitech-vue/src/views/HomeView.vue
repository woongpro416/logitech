<script setup>
import { reactive, computed, ref } from "vue";
import { getItems } from "@/services/itemService";
import Card from "@/components/ItemCard.vue";
import { useRouter } from "vue-router";

const router = useRouter()
const state = reactive({ items: [] })
const isLoading = ref(true)

;(async function onCreated() {
  const res = await getItems();
  if (res?.status === 200) {
    state.items = res.data;
  }
  isLoading.value = false
})();

const newItems = computed(() =>
  state.items.filter(item => item.category === 'NEW').slice(0, 4)
);
const bestItems = computed(() =>
  state.items.filter(item => item.category === 'BEST').slice(0, 4)
);
const recommendItems = computed(() =>
  state.items.filter(item => item.category === 'RECOMMEND').slice(0, 4)
);
</script>

<template>
  <div class="home">


    <section class="hero-banner">
      <div class="hero-content">
        <h1 class="hero-title">Logitech 공식 스토어</h1>
        <p class="hero-subtitle">최고의 마우스, 키보드를 최저가로 만나보세요</p>
        <button class="btn btn-light btn-lg px-5 mt-2">
          전체 상품 보기 <i class="bi bi-arrow-right ms-2"></i>
        </button>
      </div>
    </section>

  
    <div v-if="isLoading" class="text-center py-5">
      <div class="spinner-border text-primary" role="status"></div>
      <p class="mt-2 text-muted">상품을 불러오는 중...</p>
    </div>

    <div v-else class="album py-5 bg-light">
      <div class="container">

        <!-- 신상품 -->
        <section class="mb-5">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="section-title mb-0">🆕 신상품</h4>
          </div>
          <div class="row row-cols-1 row-cols-lg-2 row-cols-xl-4 g-3">
            <div class="col" v-for="item in newItems" :key="item.itemId">
              <Card :item="item" />
            </div>
            <div v-if="newItems.length === 0" class="text-muted ps-2">
              등록된 신상품이 없습니다.
            </div>
          </div>
        </section>

        <!-- 베스트 -->
        <section class="mb-5">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="section-title mb-0">🏆 베스트</h4>
          </div>
          <div class="row row-cols-1 row-cols-lg-2 row-cols-xl-4 g-3">
            <div class="col" v-for="item in bestItems" :key="item.itemId">
              <Card :item="item" />
            </div>
            <div v-if="bestItems.length === 0" class="text-muted ps-2">
              등록된 베스트 상품이 없습니다.
            </div>
          </div>
        </section>

        <!-- 추천 상품 -->
        <section class="mb-5">
          <div class="d-flex justify-content-between align-items-center mb-3">
            <h4 class="section-title mb-0">⭐ 추천 상품</h4>
          </div>
          <div class="row row-cols-1 row-cols-lg-2 row-cols-xl-4 g-3">
            <div class="col" v-for="item in recommendItems" :key="item.itemId">
              <Card :item="item" />
            </div>
            <div v-if="recommendItems.length === 0" class="text-muted ps-2">
              등록된 추천 상품이 없습니다.
            </div>
          </div>
        </section>

      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.hero-banner {
  background: linear-gradient(135deg, #0d6efd 0%, #0043a8 100%);
  color: white;
  padding: 80px 20px;
  text-align: center;
}

.hero-title {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 16px;
}

.hero-subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
  margin-bottom: 8px;
}

.section-title {
  font-weight: bold;
  border-left: 4px solid #0d6efd;
  padding-left: 10px;
}
</style>

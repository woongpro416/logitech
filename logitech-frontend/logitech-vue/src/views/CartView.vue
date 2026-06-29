<script setup>
import { getCarts, deleteItem } from "../services/cartService";
import { reactive, onMounted, computed } from "vue";
import { useCartStore } from "../store/cartStore";
import { useRouter } from "vue-router";
import { resolveMediaUrl } from "../utils/mediaUrl";

const state = reactive({
  items: [],
});

const load = async () => {
  const res = await getCarts();
  if (res && res.status === 200) {
    state.items = res.data;
  }
};

const remove = async (cartId) => {
  if (!confirm("해당 상품을 장바구니에서 삭제하시겠습니까?")) return;
  const res = await deleteItem(cartId);
  if (res && res.status === 200) {
    window.alert("삭제되었습니다.");
    await load();
  } else {
    window.alert("삭제 처리 중 오류가 발생했습니다.");
  }
};

//  총 금액 계산
const totalPrice = computed(() =>
  state.items.reduce((sum, i) => sum + i.price * i.quantity, 0)
)

const cartStore = useCartStore();
const router = useRouter();

const goToOrder = async () => {
  //  CartResponseDto 구조에 맞게 변환
  const orderItems = state.items.map(i => ({
    cartId: i.cartId,
    item: {
      itemId: i.itemId,
      itemName: i.itemName,
      price: i.price
    }
  }))
  await cartStore.setOrderItems(orderItems)
  router.push("/orders")
}

onMounted(load);
</script>

<template>
  <div class="carts">
    <div class="container">
      <template v-if="state.items.length">
        <ul class="items">
          <li v-for="i in state.items" :key="i.cartId">
            <img :alt="`상품사진(${i.itemName})`" :src="resolveMediaUrl(i.imgPath)" />
            <b class="name">{{ i.itemName }}</b>
            <span class="price">{{ i.price.toLocaleString() }}원</span>
            <!-- ✅ 수량 표시 -->
            <span class="quantity">수량: {{ i.quantity }}</span>
            <span class="remove float-end" @click="remove(i.cartId)" title="삭제">
              &times;
            </span>
          </li>
        </ul>

        
        <div class="total-price text-center my-3">
          <span class="fs-5">총 금액: </span>
          <b class="fs-5 text-primary">{{ totalPrice.toLocaleString() }}원</b>
        </div>

        <div class="act">
          <button class="btn btn-primary" @click="goToOrder">주문하기</button>
        </div>
      </template>
      <div class="text-center py-5" v-else>장바구니가 비어있습니다.</div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.carts {
  .items {
    list-style: none;
    margin: 0;
    padding: 0;
    li {
      border: 1px solid #eee;
      margin-top: 25px;
      margin-bottom: 25px;
      padding: 10px;
      display: flex;
      align-items: center;
      gap: 15px;
    }
    img {
      width: 150px;
      height: 150px;
      object-fit: cover;
    }
    .name {
      flex: 1;
    }
    .price {
      color: #0d6efd;
      font-weight: bold;
    }
    .quantity {
      color: #666;
    }
    .remove {
      cursor: pointer;
      font-size: 30px;
      padding: 5px 15px;
    }
  }
  .act .btn {
    width: 300px;
    display: block;
    margin: 0 auto;
    padding: 30px 50px;
    font-size: 20px;
  }
}
</style>

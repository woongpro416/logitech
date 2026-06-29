<script setup>
import { addItem } from "@/services/cartService";
import { useRouter } from "vue-router";
import { computed } from "vue";
import { resolveMediaUrl } from "@/utils/mediaUrl";

const props = defineProps({
  item: {
    itemId: Number,
    imgPath: String,
    itemName: String,
    price: Number,
  },
});

//라우터 객체
const router = useRouter();

//장바구니에 상품 담기
const put = async () => {
  console.log("버튼 클릭됨", props.item.itemId);
  const res = await addItem(props.item.itemId);
  console.log("res: ", res);

  if (
    res.status === 200 &&
    window.confirm("장바구니에 상품을 담았습니다. 장바구니로 이동하시겠습니까?")
  ) {
    await router.push("/carts");
  }
};
</script>

<template>
  <div class="card shadow-sm">
    <span
      class="img"
      :style="{ backgroundImage: `url(${resolveMediaUrl(props.item.imgPath)})` }"
      :aria-label="`상품사진(${props.item.name})`"
    ></span>
    <div class="card-body">
      <p class="card-text" style="font-weight: bold;">{{ props.item.itemName }}</p>
      <div class="d-flex justify-content-between align-items-center">
      <div class="d-flex gap-2">
        <button
          class="btn btn-outline-secondary btn-sm"
          @click="router.push(`/items/${props.item.itemId}`)">
          상세보기
        </button>
        <button class="btn btn-primary btn-sm" @click="put()">장바구니 담기</button>
        </div>
        <small class="price text-muted">{{ props.item.price }}원</small>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.card {
  .img {
    display: inline-block;
    width: 100%;
    height: 350px;
    background-size: cover;
    background-position: center;
  }

  .card-body .price {
    font-weight: bold;
  }
}
</style>

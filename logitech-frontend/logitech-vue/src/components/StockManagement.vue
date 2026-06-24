<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/axios.js'

const items = ref([])

onMounted(() => loadItems())

const loadItems = async () => {
    const res = await api.get('/items/list').catch(e => e.response)
    if (res?.status === 200) items.value = res.data
}

const updateStock = async (item) => {
    const res = await api.patch(`/items/stock/${item.itemId}`, null, {
        params: { stock: item.stock }
    }).catch(e => e.response)
    if (res?.status === 200 || res?.status === 204) {
        alert('재고가 수정되었습니다.')
    } else {
        alert('재고 수정 중 오류가 발생했습니다.')
    }
}
</script>

<template>
  <div class="card shadow-sm">
    <div class="card-body p-0">
      <table class="table table-hover mb-0">
        <thead class="table-dark">
          <tr>
            <th>상품ID</th>
            <th>상품명</th>
            <th>가격</th>
            <th>현재 재고</th>
            <th>재고 수정</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in items" :key="item.itemId">
            <td>{{ item.itemId }}</td>
            <td>{{ item.itemName }}</td>
            <td>{{ item.price.toLocaleString() }}원</td>
            <td>
              <span :class="item.stock <= 5 ? 'badge bg-danger' : 'badge bg-success'">
                {{ item.stock }}개
              </span>
            </td>
            <td>
              <div class="d-flex gap-2 align-items-center">
                <input v-model.number="item.stock" type="number"
                  class="form-control form-control-sm" style="width: 80px" min="0" />
                <button class="btn btn-sm btn-outline-primary"
                  @click="updateStock(item)">
                  수정
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

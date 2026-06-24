<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '@/api/axios.js'


const orders = ref([])
const filterStatus = ref('ALL')

onMounted(() => loadOrders())

const loadOrders = async () => {
    const res = await api.get('/orders/list').catch(e => e.response)
    if (res?.status === 200) orders.value = res.data
}

// 상태별 필터링
const filteredOrders = computed(() => {
    if (filterStatus.value === 'ALL') return orders.value
    return orders.value.filter(o => o.status === filterStatus.value)
})

// 통계
const totalAmount = computed(() =>
    orders.value
        .filter(o => o.status === 'ORDER')
        .reduce((sum, o) => sum + o.amount, 0)
)
const orderCount = computed(() => orders.value.filter(o => o.status === 'ORDER').length)
const cancelCount = computed(() => orders.value.filter(o => o.status === 'CANCEL').length)

const formatDate = (dateStr) => {
    if (!dateStr) return '-'
    return new Date(dateStr).toLocaleString('ko-KR')
}

const statusBadge = (status) => {
    switch (status) {
        case 'ORDER': return 'badge bg-primary'
        case 'CANCEL': return 'badge bg-danger'
        case 'COMPLETE': return 'badge bg-success'
        default: return 'badge bg-secondary'
    }
}

const statusLabel = (status) => {
    switch (status) {
        case 'ORDER': return '주문완료'
        case 'CANCEL': return '취소됨'
        case 'COMPLETE': return '배송완료'
        default: return status
    }
}
</script>

<template>
  <div class="container-fluid py-4">
    <h3 class="mb-4">주문 관리</h3>

    <div class="row g-3 mb-4">
      <div class="col-md-4">
        <div class="card border-0 shadow-sm bg-primary text-white">
          <div class="card-body d-flex justify-content-between align-items-center">
            <div>
              <div class="fs-6 opacity-75">총 주문수</div>
              <div class="fs-2 fw-bold">{{ orderCount }}</div>
            </div>
            <i class="bi bi-cart-check fs-1 opacity-50"></i>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card border-0 shadow-sm bg-success text-white">
          <div class="card-body d-flex justify-content-between align-items-center">
            <div>
              <div class="fs-6 opacity-75">총 판매금액</div>
              <div class="fs-2 fw-bold">{{ totalAmount.toLocaleString() }}원</div>
            </div>
            <i class="bi bi-cash-stack fs-1 opacity-50"></i>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card border-0 shadow-sm bg-danger text-white">
          <div class="card-body d-flex justify-content-between align-items-center">
            <div>
              <div class="fs-6 opacity-75">취소 건수</div>
              <div class="fs-2 fw-bold">{{ cancelCount }}</div>
            </div>
            <i class="bi bi-x-circle fs-1 opacity-50"></i>
          </div>
        </div>
      </div>
    </div>

    <div class="mb-3 d-flex gap-2">
      <button class="btn btn-sm"
        :class="filterStatus === 'ALL' ? 'btn-dark' : 'btn-outline-dark'"
        @click="filterStatus = 'ALL'">전체</button>
      <button class="btn btn-sm"
        :class="filterStatus === 'ORDER' ? 'btn-primary' : 'btn-outline-primary'"
        @click="filterStatus = 'ORDER'">주문완료</button>
      <button class="btn btn-sm"
        :class="filterStatus === 'CANCEL' ? 'btn-danger' : 'btn-outline-danger'"
        @click="filterStatus = 'CANCEL'">취소됨</button>
      <button class="btn btn-sm"
        :class="filterStatus === 'COMPLETE' ? 'btn-success' : 'btn-outline-success'"
        @click="filterStatus = 'COMPLETE'">배송완료</button>
    </div>

    <!-- 주문 목록 -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-hover mb-0">
          <thead class="table-dark">
            <tr>
              <th>주문번호</th>
              <th>주문자</th>
              <th>결제수단</th>
              <th>결제금액</th>
              <th>주문상품</th>
              <th>상태</th>
              <th>주문일시</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="filteredOrders.length === 0">
              <td colspan="7" class="text-center py-4 text-muted">주문이 없습니다.</td>
            </tr>
            <tr v-for="order in filteredOrders" :key="order.orderId">
              <td>#{{ order.orderId }}</td>
              <td>{{ order.name }}</td>
              <td>{{ order.payment === 'card' ? '카드' : '무통장입금' }}</td>
              <td>{{ order.amount.toLocaleString() }}원</td>
              <td>{{ order.itemIds }}</td>
              <td>
                <span :class="statusBadge(order.status)">
                  {{ statusLabel(order.status) }}
                </span>
              </td>
              <td>{{ formatDate(order.createAt) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    
  </div>
</template>

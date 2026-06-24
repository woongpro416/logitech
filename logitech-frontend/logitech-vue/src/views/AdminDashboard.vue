<template>
  <div class="container-fluid py-4">
    <h3 class="mb-4"><i class="bi bi-speedometer2 me-2"></i>관리자 대시보드</h3>

    <!-- 통계 카드 -->
    <div class="row g-3 mb-4">
      <div class="col-md-4">
        <div class="card border-0 shadow-sm bg-primary text-white">
          <div class="card-body d-flex justify-content-between align-items-center">
            <div>
              <div class="fs-6 opacity-75">총 회원수</div>
              <div class="fs-2 fw-bold">{{ stats.totalMembers }}</div>
            </div>
            <i class="bi bi-people fs-1 opacity-50"></i>
          </div>
          <div class="card-footer bg-transparent border-0">
            <router-link
              to="/admin/members"
              class="text-white text-decoration-none small"
            >
              회원 관리 바로가기 <i class="bi bi-arrow-right"></i>
            </router-link>
          </div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card border-0 shadow-sm bg-success text-white">
          <div class="card-body d-flex justify-content-between align-items-center">
            <div>
              <div class="fs-6 opacity-75">총 주문수</div>
              <div class="fs-2 fw-bold">{{ stats.totalOrders }}</div>
            </div>
            <i class="bi bi-cart-check fs-1 opacity-50"></i>
          </div>
          <div class="card-footer bg-transparent border-0">
            <router-link to="/admin/orders" class="text-white text-decoration-none small">
              주문 관리 바로가기 <i class="bi bi-arrow-right"></i>
            </router-link>
          </div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card border-0 shadow-sm bg-warning text-white">
          <div class="card-body d-flex justify-content-between align-items-center">
            <div>
              <div class="fs-6 opacity-75">총 상품수</div>
              <div class="fs-2 fw-bold">{{ stats.totalItems }}</div>
            </div>
            <i class="bi bi-box-seam fs-1 opacity-50"></i>
          </div>
          <div class="card-footer bg-transparent border-0">
            <router-link to="/admin/items" class="text-white text-decoration-none small">
              상품 관리 바로가기 <i class="bi bi-arrow-right"></i>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 최근 현황 -->
    <div class="row g-3">
      <!-- 최근 가입 회원 -->
      <div class="col-md-6">
        <div class="card shadow-sm">
          <div class="card-header bg-white fw-bold">
            <i class="bi bi-person-plus me-2 text-primary"></i>최근 가입 회원
          </div>
          <div class="card-body p-0">
            <table class="table table-hover mb-0">
              <thead class="table-light">
                <tr>
                  <th>이름</th>
                  <th>아이디</th>
                  <th>등급</th>
                  <th>가입일</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="recentMembers.length === 0">
                  <td colspan="4" class="text-center text-muted py-3">
                    데이터가 없습니다.
                  </td>
                </tr>
                <tr v-for="member in recentMembers" :key="member.memberId">
                  <td>{{ member.name }}</td>
                  <td>{{ member.loginId }}</td>
                  <td>
                    <span
                      :class="
                        member.role === 'ADMIN' ? 'badge bg-danger' : 'badge bg-primary'
                      "
                    >
                      {{ member.role }}
                    </span>
                  </td>
                  <td>{{ formatDate(member.createdAt) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 최근 주문 -->
      <div class="col-md-6">
        <div class="card shadow-sm">
          <div class="card-header bg-white fw-bold">
            <i class="bi bi-bag-check me-2 text-success"></i>최근 주문 내역
          </div>
          <div class="card-body p-0">
            <table class="table table-hover mb-0">
              <thead class="table-light">
                <tr>
                  <th>주문번호</th>
                  <th>회원명</th>
                  <th>금액</th>
                  <th>상태</th>
                  <th>주문일</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="recentOrders.length === 0">
                  <td colspan="5" class="text-center text-muted py-3">
                    데이터가 없습니다.
                  </td>
                </tr>
                <tr v-for="order in recentOrders" :key="order.orderId">
                  <td>#{{ order.orderId }}</td>
                  <td>{{ order.name }}</td>
                  <td>{{ order.amount.toLocaleString() }}원</td>
                  <td>
                    <span :class="statusBadge(order.status)">{{ order.status }}</span>
                  </td>
                  <td>{{ formatDate(order.createAt) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- 관리 메뉴 바로가기 -->
    <div class="row g-3 mt-2">
      <div class="col-12">
        <div class="card shadow-sm">
          <div class="card-header bg-white fw-bold">
            <i class="bi bi-grid me-2"></i>관리 메뉴
          </div>
          <div class="card-body">
            <div class="row g-2">
              <div class="col-6 col-md-3">
                <router-link to="/admin/members" class="btn btn-outline-primary w-100">
                  <i class="bi bi-people me-1"></i>회원 관리
                </router-link>
              </div>
              <div class="col-6 col-md-3">
                <router-link to="/admin/items" class="btn btn-outline-warning w-100">
                  <i class="bi bi-box-seam me-1"></i>상품 관리
                </router-link>
              </div>
              <div class="col-6 col-md-3">
                <router-link to="/admin/orders" class="btn btn-outline-success w-100">
                  <i class="bi bi-cart me-1"></i>주문 관리
                </router-link>
              </div>
              <div class="col-6 col-md-3">
                <router-link to="/admin/reviews" class="btn btn-outline-info w-100">
                  <i class="bi bi-star me-1"></i>리뷰 관리
                </router-link>
              </div>
              <div class="col-6 col-md-3">
                <router-link to="/admin/qna" class="btn btn-outline-secondary w-100">
                  <i class="bi bi-chat-dots me-1"></i>Q&A 관리
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useAccountStore } from "@/store/accountStore";
import api from "@/api/axios.js";

const router = useRouter();
const accountStore = useAccountStore();

const stats = ref({ totalMembers: 0, totalOrders: 0, totalItems: 0 });
const recentMembers = ref([]);
const recentOrders = ref([]);

onMounted(async () => {
  // 관리자 아닌 경우 차단
  if (accountStore.loginUser?.role !== "ADMIN") {
    router.push("/");
    return;
  }

  const res = await api.get("/admin/dashboard").catch((e) => e.response);
  if (res?.status === 200) {
    const data = res.data;
    stats.value.totalMembers = data.totalMembers;
    stats.value.totalOrders = data.totalOrders;
    stats.value.totalItems = data.totalItems;
    recentMembers.value = data.recentMembers;
    recentOrders.value = data.recentOrders;
  }
});

const formatDate = (dateStr) => {
  if (!dateStr) return "-";
  return new Date(dateStr).toLocaleDateString("ko-KR");
};

const statusBadge = (status) => {
  switch (status) {
    case "ORDER":
      return "badge bg-primary";
    case "CANCEL":
      return "badge bg-danger";
    case "COMPLETE":
      return "badge bg-success";
    default:
      return "badge bg-secondary";
  }
};
</script>

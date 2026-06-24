import { createRouter, createWebHistory } from 'vue-router'
import { useAccountStore } from '@/store/accountStore'


const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            component: () => import('@/views/HomeView.vue')
        },
        {
            path: '/join',
            name: '/join',
            component: () => import('@/views/JoinView.vue')
        },
        {
            path: '/login',
            name: '/login',
            component: () => import('@/views/LoginView.vue')
        },
        {
            path: '/carts',
            name: '/carts',
            component: () => import('@/views/CartView.vue')
        },
        {
            path: '/orders',
            name: '/orders',
            component: () => import('@/views/OrderView.vue')
        },
        {
            path: '/history',
            name: '/history',
            component: () => import('@/views/History.vue')
        },
        {
            path: '/history/:id',
            name: '/history-detail',
            component: () => import('@/views/OrderDetail.vue')
        },
        {
            path: '/mypage',
            name: '/mypage',
            component: () => import('@/views/MyPageView.vue')
        },
        {
            path: '/mypage/edit',
            name: '/mypage-edit',
            component: () => import('@/views/MyPageEditView.vue')
        },
        {
            path: '/admin',
            name: '/admin',
            component: () => import('@/views/AdminDashboard.vue')
        },
        {
            path: '/admin/members',
            name: '/admin-members',
            component: () => import('@/views/AdminMemberView.vue')
        },
        {
            path: '/items/:itemId',
            name: '/item-detail',
            component: () => import('@/views/ItemDetailView.vue')
        },
        {
            path: '/admin/items',
            name: '/admin-items',
            component: () => import('@/views/AdminItemView.vue')
        },
        {
            path: '/admin/reviews',
            name: '/admin-reviews',
            component: () => import('@/views/AdminReviewView.vue')
        },
        {
            path: '/qna',
            name: '/qna',
            component: () => import('@/views/QnaView.vue')
        },
        {
            path: '/qna/:id',
            name: '/qna-detail',
            component: () => import('@/views/QnaDetailView.vue')
        },
        {
            path: '/admin/qna',
            name: '/admin-qna',
            component: () => import('@/views/AdminQnaView.vue')
        },
        {
            path: '/admin/orders',
            name: '/admin-orders',
            component: () => import('@/views/AdminOrderView.vue')
        },
        {
            path: '/items',
            name: '/items',
            component: () => import('@/views/ItemListView.vue')
        }
    ]
})

router.beforeEach((to, from) => {
    const accountStore = useAccountStore();

    //관리자 전용 페이지
    if (to.path.startsWith('/admin')) {
        if (!accountStore.loggedIn) return '/login'
        if (accountStore.loginUser?.role !== 'ADMIN') return '/'
    }

    const authRequired = ['/history', '/carts', '/orders', '/mypage'];
    const needsAuth = authRequired.some(path => to.path.startsWith(path));
    if (needsAuth && !accountStore.loggedIn) {
        return '/login';
    }
});

export default router;

import api from '@/api/axios.js';

//회원가입
export const join = (args) => {
    return api.post("/members/join", args);
};

//로그인
export const login = (args) => {
    return api.post("/members/login", args).catch(e => e.response);
};

//로그인 여부 확인
export const check = () => {
    return api.get("/members/check").catch(e => e.response);
};

//로그아웃
export const logout = () => {
    return api.post("/members/logout").catch(e => e.response);
};
// 회원 상세 조회
export const getMember = (memberId) => {
    return api.get(`/members/detail/${memberId}`).catch(e => e.response);
}

// 회원 정보 수정
export const updateMember = (memberId, data) => {
    return api.put(`/members/edit/${memberId}`, data).catch(e => e.response);
}

// 회원 탈퇴
export const deleteMember = (memberId) => {
    return api.delete(`/members/delete/${memberId}`).catch(e => e.response);
}

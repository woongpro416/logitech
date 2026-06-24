import api from "@/api/axios.js";

// 회원 목록 조회
export const getMemberList = () => {
    return api.get("/members/list").catch(e => e.response)
}

// 회원 검색
export const searchMember = (keyword) => { 
    return api.get(`/members/search`, { params: { keyword } }).catch(e => e.response)
}

// 회원 강퇴
export const deleteMember = (memberId) => { 
    return api.delete(`/members/delete/${memberId}`).catch(e => e.response)
}

// 회원 등급 변경
export const changeRole = (memberId, role) => {
    return api.patch(`/members/role/${memberId}`, null, { params: { role } }).catch(e => e.response)
}


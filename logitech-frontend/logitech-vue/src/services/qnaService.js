import api from "@/api/axios.js"

//전체 목록
export const getQnaList = () => {
    return api.get('/qna/list').catch(e => e.response)
}

//제목 검색
export const searchQna = (keyword) => {
    return api.get('/qna/search', { params: { keyword }}).catch(e => e.response)
}

//상세보기
export const getQnaDetail = (id) => {
    return api.get(`/qna/detail/${id}`).catch(e => e.response)
}

//질문 등록
export const addQuestion = (data) => {
    return api.post('/qna/question', data).catch(e => e.response)
}

//답변 등록 (관리자)
export const addAnswer = (parentId, data) => {
    return api.post(`/qna/answer/${parentId}`, data).catch(e => e.response)
}

//수정
export const updateQna = (id, data) => {
    return api.put(`/qna/update/${id}`, data).catch(e => e.response)
}

//삭제
export const deleteQna = (id) => {
    return api.delete(`/qna/delete/${id}`).catch(e => e.response)
}

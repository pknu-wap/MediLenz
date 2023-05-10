package com.android.mediproject.core.model.remote.recall


/**
 * 상세 리콜 폐기 데이터
 *
 * @property bIZRNO 사업자등록번호
 * @property eNTRPS 업체명
 * @property eNTRPSADRES 업체 주소
 * @property eNTRPSTELNO 업체 전화번호
 * @property mNFCTURDT 제조일자
 * @property mNFCTURNO 제조번호
 * @property oPENENDDATE 폐기일자
 * @property pACKNGUNIT 포장단위
 * @property pRDUCT 제품명
 * @property rECALLCOMMANDDATE 리콜명령일자
 * @property rM 비고
 * @property rTRVLCMMNDDT 폐기명령일자
 * @property rTRVLRESN  폐기사유
 * @property uSGPD 기간
 *
 */
data class DetailRecallSuspensionItemDto(
    val bIZRNO: String?, // 1278641862
    val eNTRPS: String?, // (주)자연세상
    val eNTRPSADRES: String?, // 경기도 포천시 소흘읍 호국로481번길 21-16
    val eNTRPSTELNO: String?, // 0315443091
    val mNFCTURDT: String?, // 20211001
    val mNFCTURNO: String?, // 330-061-21
    val oPENENDDATE: String?, // 20250922
    val pACKNGUNIT: String?, // 자사포장단위
    val pRDUCT: String?, // 자연세상갈근
    val rECALLCOMMANDDATE: String?, // 20220923
    val rM: String?, // 비고
    val rTRVLCMMNDDT: String?, // 20221004000000
    val rTRVLRESN: String?, // 정량법 부적합
    val uSGPD: String? // 제조일로부터36개월
)

fun DetailRecallSuspensionResponse.Body.Item.Item.toDto(): DetailRecallSuspensionItemDto =
    DetailRecallSuspensionItemDto(
        bIZRNO = bIZRNO,
        eNTRPS = eNTRPS,
        eNTRPSADRES = eNTRPSADRES,
        eNTRPSTELNO = eNTRPSTELNO,
        mNFCTURDT = mNFCTURDT,
        mNFCTURNO = mNFCTURNO,
        oPENENDDATE = oPENENDDATE,
        pACKNGUNIT = pACKNGUNIT,
        pRDUCT = pRDUCT,
        rECALLCOMMANDDATE = rECALLCOMMANDDATE,
        rM = rM,
        rTRVLCMMNDDT = rTRVLCMMNDDT,
        rTRVLRESN = rTRVLRESN,
        uSGPD = uSGPD
    )
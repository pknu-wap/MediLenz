package com.android.mediproject.core.model.medicine.common.cancel

import com.android.mediproject.core.model.toLocalDate

class MedicineCancelStatusMapper {
    companion object {
        private const val cancelDateFormat = "yyyyMMdd"
        fun map(cancelName: String, cancelDate: String = ""): MedicineCancelStatus {
            return when (MedicineCancelStatusType.statusOf(cancelName)) {
                MedicineCancelStatusType.CANCELED -> Canceled(cancelDate.toLocalDate(cancelDateFormat))
                MedicineCancelStatusType.EXPIRED -> Expired(cancelDate.toLocalDate(cancelDateFormat))
                MedicineCancelStatusType.NORMAL -> Normal()
            }
        }
    }
}

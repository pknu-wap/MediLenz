package com.android.mediproject.core.model.medicine.common.cancel

import androidx.annotation.StringRes
import com.android.mediproject.core.model.DateTimeValue
import com.android.mediproject.core.model.R

enum class MedicineCancelStatusType(val status: String, @StringRes val statusStringResId: Int) {
    CANCELED("취하", R.string.medicineCancelStatus_canceled), EXPIRED(
        "유효기간만료",
        R.string.medicineCancelStatus_expired,
    ),
    NORMAL("정상", R.string.medicineCancelStatus_normal);

    companion object {
        fun statusOf(status: String) = values().find { it.status == status } ?: throw IllegalArgumentException()
    }
}

interface MedicineCancelStatus {
    @get:StringRes val statusStringId: Int
}

data class Canceled(val cancelDate: DateTimeValue) : MedicineCancelStatus {
    override val statusStringId: Int = MedicineCancelStatusType.CANCELED.statusStringResId
}

class Expired(val cancelDate: DateTimeValue) : MedicineCancelStatus {
    override val statusStringId: Int = MedicineCancelStatusType.EXPIRED.statusStringResId
}

class Normal : MedicineCancelStatus {
    override val statusStringId: Int = MedicineCancelStatusType.NORMAL.statusStringResId
}

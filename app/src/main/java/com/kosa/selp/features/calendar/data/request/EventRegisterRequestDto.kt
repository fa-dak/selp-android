import com.kosa.selp.features.survey.model.AnniversaryType

data class EventRegisterRequestDto(
    val eventDate: String,
    val eventName: String?,
    val eventType: AnniversaryType?,
    val receiverInfoId: Long?,
    val notificationDaysBefore: Int? = null
)

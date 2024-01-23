import com.usecase.picture_selector.IPictureSelect
import com.usecase.picture_selector.AndroidPictureSelect

actual val pictureSelect: IPictureSelect = AndroidPictureSelect(ActivityManager.getCurrentActivity())
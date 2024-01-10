import com.usecase.picture_selector.IPictureSelect
import com.usecase.picture_selector.PictureSelectImpl

actual val pictureSelect: IPictureSelect = PictureSelectImpl(ActivityManager.getCurrentActivity())
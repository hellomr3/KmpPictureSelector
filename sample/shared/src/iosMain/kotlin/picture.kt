import com.usecase.picture_selector.IPictureSelect
import com.usecase.picture_selector.PictureSelectImpl
import platform.UIKit.UIApplication

val currentController = UIApplication.sharedApplication.keyWindow?.rootViewController

actual val pictureSelect:IPictureSelect = PictureSelectImpl(currentController = currentController)
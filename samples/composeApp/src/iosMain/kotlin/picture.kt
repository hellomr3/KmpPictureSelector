import com.usecase.picture_selector.IPictureSelect
import com.usecase.picture_selector.IOSPictureSelect
import platform.UIKit.UIApplication

val currentController = UIApplication.sharedApplication.keyWindow?.rootViewController

actual val pictureSelect:IPictureSelect = IOSPictureSelect(currentController = currentController)
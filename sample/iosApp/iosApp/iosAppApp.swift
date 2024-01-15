import UIKit
import shared

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // 创建一个登录视图控制器并将其设置为窗口的根视图控制器
        window = UIWindow(frame: UIScreen.main.bounds)
        let controller = Main_iosKt.MainViewController()
        window?.rootViewController = controller

        window?.makeKeyAndVisible()
        return true
    }

}

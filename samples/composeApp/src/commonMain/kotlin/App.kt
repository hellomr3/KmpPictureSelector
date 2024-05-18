import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.picture_selector.compose.rememberPictureSelect
import com.usecase.picture_selector.Media
import com.usecase.picture_selector.PictureSelectParams
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun App() {
    val scope = rememberCoroutineScope()
    val pictureSelector = rememberPictureSelect()
    Scaffold(modifier = Modifier.statusBarsPadding()) {
        var pictureMedia by remember { mutableStateOf<Media?>(null) }
        Column {
            Button(onClick = {
                scope.launch {
                    pictureMedia =
                        pictureSelector.takePhoto(
                            params = PictureSelectParams(
                                maxImageNum = 1,
                                isCrop = true
                            )
                        )
                            .firstOrNull()
                            ?.getOrNull()
                }
            }) {
                Text("拍摄照片", modifier = Modifier.padding(horizontal = 24.dp))
            }
            Button(onClick = {
                scope.launch {
                    pictureMedia =
                        pictureSelector.takePhoto(
                            params = PictureSelectParams(
                                maxImageNum = 0,
                                maxVideoNum = 2
                            )
                        ).firstOrNull()
                            ?.getOrNull()
                }
            }) {
                Text("拍摄视频", modifier = Modifier.padding(horizontal = 24.dp))
            }
            Button(onClick = {
                scope.launch {
                    pictureMedia =
                        pictureSelector.selectPhoto(
                            params = PictureSelectParams(
                                maxImageNum = 1,
                                maxVideoNum = 1,
                                isCrop = true
                            )
                        ).firstOrNull()
                            ?.getOrNull()?.firstOrNull()
                }
            }) {
                Text("选择图库", modifier = Modifier.padding(horizontal = 24.dp))
            }
            Text("path:${pictureMedia?.path}")
            AsyncImage(
                model = pictureMedia?.preview?.toByteArray(),
                contentDescription = "Image",
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            )
        }
    }
}
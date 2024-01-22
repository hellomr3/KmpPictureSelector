import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.usecase.picture_selector.PictureSelectParams
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    val scope = rememberCoroutineScope()
    Scaffold (modifier = Modifier.statusBarsPadding()){
        Column {
            Button(onClick = {
                scope.launch {
                    pictureSelect.takePhoto(params = PictureSelectParams(1)).firstOrNull()
                }
            }){
                Text("拍照", modifier = Modifier.padding(24.dp).fillMaxWidth())
            }
            Button(onClick = {
                scope.launch {
                    pictureSelect.selectPhoto(params = PictureSelectParams(1)).firstOrNull()
                }
            }){
                Text("选择图库", modifier = Modifier.padding(24.dp).fillMaxWidth())
            }
        }
    }
}
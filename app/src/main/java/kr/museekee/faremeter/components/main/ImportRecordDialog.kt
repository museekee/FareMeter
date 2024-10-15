package kr.museekee.faremeter.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kr.museekee.faremeter.dataPath
import java.io.File


@Composable
fun ImportRecordDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .width(375.dp)
                .height(600.dp)
                .padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF202020)
            ),
            shape = RoundedCornerShape(25.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
//                val intent = Intent(Intent.ACTION_GET_CONTENT)
//                intent.setType("*/*") // 모든 파일 형식 선택 가능
//                intent.addCategory(Intent.CATEGORY_OPENABLE)
//                ActivityResultLauncher(Intent.createChooser(intent, "파일 선택")

                val fileList = mutableListOf<String>()
                val dataDir = File(dataPath)
                val files = dataDir.listFiles() ?: arrayOf<File>()
                files.forEach {
                    fileList.add(it.name)
                }
                fileList.map {
                    Text(text = it)
                }
            }
        }
    }
}
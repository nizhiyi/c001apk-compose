package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.c001apk.compose.constant.Constants.PREFIX_HTTP
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.ui.component.CoilLoader
import com.example.c001apk.compose.ui.component.ImageView
import com.example.c001apk.compose.ui.component.LinkText
import com.example.c001apk.compose.util.ImageShowUtil
import com.example.c001apk.compose.util.density
import com.example.c001apk.compose.util.longClick
import com.example.c001apk.compose.util.screenWidth

/**
 * Created by bggRGjQaUbCoE on 2024/6/19
 */
@Composable
fun ChatLeftCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onGetImageUrl: (String) -> Unit,
    onLongClick: (String, String, String) -> Unit,
    onViewUser: (String) -> Unit,
    onClearFocus: () -> Unit,
) {

    val maxWidth = (screenWidth - 142 * density) / density

    Row(
        modifier = modifier
            .fillMaxWidth()
            .longClick {
                onLongClick(data.id.orEmpty(), data.message.orEmpty(), data.messagePic.orEmpty())
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {

        CoilLoader(
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
                .clickable { onViewUser(data.messageUid.orEmpty()) },
            url = data.fromUserAvatar
        )

        if (!data.message.isNullOrEmpty()) {
            LinkText(
                text = data.message,
                modifier = Modifier
                    .widthIn(max = maxWidth.dp)
                    .padding(start = 10.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 12.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                    .padding(horizontal = 10.dp, vertical = 12.dp)
            )
        }

        if (!data.messagePic.isNullOrEmpty()) {
            if (!data.messagePic.startsWith(PREFIX_HTTP)) {
                onGetImageUrl(data.id.orEmpty())
            }
            val imageLp by lazy {
                ImageShowUtil.getImageLp(
                    if (data.messagePic.startsWith(PREFIX_HTTP))
                        data.messagePic.substring(0, data.messagePic.indexOfFirst { it == '?' })
                    else data.messagePic
                )
            }
            val imageWidth by lazy { maxWidth / 2f }
            val imageHeight by lazy { imageWidth * imageLp.second / imageLp.first }
            ImageView(
                url = data.messagePic,
                isChat = true,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                    .width((maxWidth / 2f).dp)
                    .height(imageHeight.dp),
                onClearFocus = onClearFocus,
            )
        }

    }

}
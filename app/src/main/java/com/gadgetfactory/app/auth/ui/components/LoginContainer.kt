package com.gadgetfactory.app.auth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gadgetfactory.app.ui.components.SheetHandle

@Composable
fun LoginContainer(
    buttonsData: List<LoginButtonData>,
    onProviderClick: (AuthProvider) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .clip(RoundedCornerShape(topEnd = 23.dp, topStart = 23.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SheetHandle(modifier = Modifier.fillMaxWidth(0.4f))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = buttonsData,
                key = { it.provider.providerCode },
            ) {
                LoginButton(
                    modifier = Modifier.fillMaxWidth(),
                    data = it,
                    onClick = onProviderClick,
                )
            }
        }
    }
}

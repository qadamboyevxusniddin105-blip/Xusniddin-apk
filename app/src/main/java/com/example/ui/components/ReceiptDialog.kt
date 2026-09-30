package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.OrderEntity
import com.example.util.CurrencyFormatter

@Composable
fun ReceiptDialog(
    order: OrderEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .testTag("receipt_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Text(
                            text = "Buyurtma Cheki",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("receipt_close_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Yopish")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Realistic Paper Receipt Body
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Success Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .background(
                                    Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "MUVAFFAQIYATLI QABUL QILINDI",
                                color = Color(0xFF2E7D32),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "MAZALI TAOMLAR OSHXONASI",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = "Rasmiy to'lov va xarid kvitansiyasi",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Receipt Number & Date
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Chek №:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = order.orderNumber,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Sana va vaqt:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = CurrencyFormatter.formatDate(order.timestamp),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        DashedDivider()
                        Spacer(modifier = Modifier.height(8.dp))

                        // Customer Details
                        ReceiptRow(label = "Xaridor:", value = order.customerName)
                        ReceiptRow(label = "Telefon:", value = order.customerPhone)
                        ReceiptRow(label = "Xizmat turi:", value = order.orderType)
                        ReceiptRow(
                            label = if (order.orderType == "Restoranda") "Stol raqami:" else "Manzil:",
                            value = order.deliveryAddressOrTable
                        )
                        ReceiptRow(label = "To'lov usuli:", value = order.paymentMethod)

                        if (order.orderNotes.isNotEmpty()) {
                            ReceiptRow(label = "Izoh:", value = order.orderNotes)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        DashedDivider()
                        Spacer(modifier = Modifier.height(10.dp))

                        // Items Table Header
                        Text(
                            text = "BUYURTMA QILINGAN TAOMLAR",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Items List
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(10.dp)
                        ) {
                            order.itemsSummary.lines().filter { it.isNotBlank() }.forEach { line ->
                                Text(
                                    text = line,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        DashedDivider()
                        Spacer(modifier = Modifier.height(10.dp))

                        // Subtotal, Service fee, Delivery fee
                        ReceiptRow(
                            label = "Taomlar summasi:",
                            value = CurrencyFormatter.format(order.subtotal)
                        )
                        ReceiptRow(
                            label = "Xizmat haqi (10%):",
                            value = CurrencyFormatter.format(order.serviceFee)
                        )
                        if (order.deliveryFee > 0) {
                            ReceiptRow(
                                label = "Yetkazib berish (Dostavka):",
                                value = CurrencyFormatter.format(order.deliveryFee)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Grand Total ("Opshi Shota") Highlight Card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "OPSHI SHOT",
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "JAMI TO'LOV",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                Text(
                                    text = CurrencyFormatter.format(order.grandTotal),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Simulated Barcode graphic
                        BarcodeLines(modifier = Modifier.fillMaxWidth().height(36.dp))

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "* Xaridingiz uchun rahmat! Yoqimli ishtaha! *",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons: Share and Dismiss
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            shareReceipt(context, order)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("receipt_share_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Ulashish", fontSize = 13.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("receipt_confirm_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Tushunarli", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

@Composable
private fun DashedDivider() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = Color.Gray.copy(alpha = 0.4f),
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
            strokeWidth = 2f
        )
    }
}

@Composable
private fun BarcodeLines(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val barCount = 48
        val spacing = width / barCount
        for (i in 0 until barCount) {
            val stroke = if (i % 3 == 0 || i % 7 == 0) 3.5f else 1.5f
            val x = i * spacing + spacing / 2
            drawLine(
                color = Color.Black.copy(alpha = 0.75f),
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = stroke
            )
        }
    }
}

private fun shareReceipt(context: Context, order: OrderEntity) {
    val text = buildString {
        appendLine("🧾 TAOMLAR MENYUSI - XARID CHEKI")
        appendLine("Chek №: ${order.orderNumber}")
        appendLine("Sana: ${CurrencyFormatter.formatDate(order.timestamp)}")
        appendLine("Xaridor: ${order.customerName} (${order.customerPhone})")
        appendLine("Buyurtma turi: ${order.orderType}")
        appendLine("Manzil/Stol: ${order.deliveryAddressOrTable}")
        appendLine("-------------------------------")
        appendLine("TAOMLAR:")
        appendLine(order.itemsSummary)
        appendLine("-------------------------------")
        appendLine("Taomlar summasi: ${CurrencyFormatter.format(order.subtotal)}")
        appendLine("Xizmat haqi (10%): ${CurrencyFormatter.format(order.serviceFee)}")
        if (order.deliveryFee > 0) {
            appendLine("Yetkazib berish: ${CurrencyFormatter.format(order.deliveryFee)}")
        }
        appendLine("===============================")
        appendLine("OPSHI SHOT (JAMI): ${CurrencyFormatter.format(order.grandTotal)}")
        appendLine("To'lov usuli: ${order.paymentMethod}")
        appendLine("Holati: ${order.status}")
        appendLine("Yoqimli ishtaha!")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Buyurtma Cheki ${order.orderNumber}")
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Chekni yuborish"))
}

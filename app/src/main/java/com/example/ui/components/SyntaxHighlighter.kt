package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SyntaxHighlightedCodeView(
    code: String,
    language: String,
    modifier: Modifier = Modifier,
    fileName: String? = null
) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val lines = remember(code) { code.lines() }
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCodeBackground),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            androidx.compose.ui.graphics.Brush.horizontalGradient(
                listOf(StudioCyanPrimary.copy(alpha = 0.5f), StudioNeonBlue.copy(alpha = 0.3f), StudioBorderDark)
            )
        )
    ) {
        Column {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF161B22))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Window dots
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFFFF5F56), RoundedCornerShape(5.dp)))
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFFFFBD2E), RoundedCornerShape(5.dp)))
                    Box(modifier = Modifier.size(10.dp).background(Color(0xFF27C93F), RoundedCornerShape(5.dp)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = fileName ?: "source.$language",
                        color = TextPrimaryDark,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("code", code)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
                        copied = true
                        coroutineScope.launch {
                            delay(2000)
                            copied = false
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copy Code",
                        tint = if (copied) StudioEmeraldSuccess else TextSecondaryDark,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Code Body with Line Numbers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .verticalScroll(verticalScroll)
                    .padding(vertical = 12.dp)
            ) {
                // Line Numbers
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp)
                        .widthIn(min = 28.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    lines.indices.forEach { index ->
                        Text(
                            text = "${index + 1}",
                            color = TextMutedDark,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 18.sp
                        )
                    }
                }

                // Vertical divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(StudioBorderDark)
                )

                // Highlighted Code Content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(horizontalScroll)
                        .padding(start = 12.dp, end = 16.dp)
                ) {
                    Column {
                        lines.forEach { line ->
                            Text(
                                text = highlightLine(line, language),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

fun highlightLine(line: String, language: String): AnnotatedString {
    return buildAnnotatedString {
        val trimmed = line.trimStart()
        if (trimmed.startsWith("//") || trimmed.startsWith("#") || trimmed.startsWith("--")) {
            pushStyle(SpanStyle(color = StudioCodeComment))
            append(line)
            pop()
            return@buildAnnotatedString
        }

        val keywords = setOf(
            "val", "var", "fun", "class", "data", "interface", "import", "package", "return", "if", "else", "when",
            "const", "let", "function", "export", "default", "from", "async", "await", "pub", "fn", "struct", "impl",
            "use", "mod", "def", "select", "from", "where", "create", "table", "alter", "drop", "insert", "into",
            "type", "guard", "extension", "public", "private", "protected", "override", "while", "for", "in"
        )

        val tokens = line.split(Regex("(?<=[^a-zA-Z0-9_])|(?=[^a-zA-Z0-9_])"))
        var inString = false

        tokens.forEach { token ->
            when {
                token.startsWith("\"") || token.endsWith("\"") || token.startsWith("'") || token.endsWith("'") -> {
                    pushStyle(SpanStyle(color = StudioCodeString))
                    append(token)
                    pop()
                }
                keywords.contains(token.lowercase()) -> {
                    pushStyle(SpanStyle(color = StudioCodeKeyword, fontWeight = FontWeight.Bold))
                    append(token)
                    pop()
                }
                token.matches(Regex("^[A-Z][a-zA-Z0-9_]*$")) -> {
                    pushStyle(SpanStyle(color = StudioCodeType))
                    append(token)
                    pop()
                }
                token.matches(Regex("^[0-9]+(\\.[0-9]+)?$")) -> {
                    pushStyle(SpanStyle(color = StudioAmberWarning))
                    append(token)
                    pop()
                }
                token.matches(Regex("^[a-z][a-zA-Z0-9_]*$")) && line.contains("$token(") -> {
                    pushStyle(SpanStyle(color = StudioCodeFunction))
                    append(token)
                    pop()
                }
                else -> {
                    pushStyle(SpanStyle(color = TextPrimaryDark))
                    append(token)
                    pop()
                }
            }
        }
    }
}

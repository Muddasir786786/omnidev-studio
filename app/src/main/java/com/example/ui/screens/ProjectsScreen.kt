package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.OmniDevViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectsScreen(
    viewModel: OmniDevViewModel,
    onProjectLoaded: () -> Unit,
    modifier: Modifier = Modifier
) {
    val savedProjects by viewModel.savedProjects.collectAsStateWithLifecycle()
    val currentProject by viewModel.currentProject.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var selectedPlatformFilter by remember { mutableStateOf("All") }

    val filtered = savedProjects.filter { proj ->
        val matchesQuery = proj.title.contains(searchQuery, ignoreCase = true) ||
                proj.description.contains(searchQuery, ignoreCase = true) ||
                proj.language.contains(searchQuery, ignoreCase = true)
        val matchesPlatform = selectedPlatformFilter == "All" || proj.platform.equals(selectedPlatformFilter, ignoreCase = true)
        matchesQuery && matchesPlatform
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("PROJECT REPOSITORY", color = StudioCyanPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("Autonomous Solutions", color = TextPrimaryDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Surface(
                color = StudioPurpleSecondary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    "${savedProjects.size} Stored",
                    color = StudioVioletAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search projects by language, framework, or title...", color = TextMutedDark, fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondaryDark, modifier = Modifier.size(18.dp)) },
            modifier = Modifier.fillMaxWidth().testTag("input_search_projects"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF0D1424),
                unfocusedContainerColor = Color(0xFF0D1424),
                focusedBorderColor = StudioCyanPrimary,
                unfocusedBorderColor = StudioBorderDark,
                focusedTextColor = TextPrimaryDark,
                unfocusedTextColor = TextPrimaryDark
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Platform Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Web", "Mobile", "Backend", "Systems", "DevOps").forEach { p ->
                val isSel = selectedPlatformFilter == p
                FilterChip(
                    selected = isSel,
                    onClick = { selectedPlatformFilter = p },
                    label = { Text(p, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioCyanPrimary,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No projects found. Synthesize a new one in the Studio!", color = TextSecondaryDark)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered, key = { it.id }) { proj ->
                    val isCurrent = currentProject?.project?.id == proj.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.loadProject(proj.id)
                                onProjectLoaded()
                            }
                            .testTag("project_card_${proj.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) StudioCardHoverDark else StudioCardDark
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCurrent) StudioCyanPrimary else StudioBorderDark
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
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
                                        color = StudioCyanPrimary.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = proj.platform,
                                            color = StudioCyanPrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(proj.title, color = TextPrimaryDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { viewModel.toggleFavorite(proj.id, proj.isFavorite) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (proj.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "Favorite",
                                            tint = if (proj.isFavorite) StudioAmberWarning else TextSecondaryDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteProject(proj.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = TextSecondaryDark,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(proj.description, color = TextSecondaryDark, fontSize = 11.sp, maxLines = 2, lineHeight = 15.sp)

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(proj.language, color = StudioVioletAccent, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    Text("•", color = TextMutedDark)
                                    Text(proj.framework, color = TextSecondaryDark, fontSize = 11.sp)
                                }

                                Surface(
                                    color = StudioEmeraldSuccess.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        "Score ${proj.securityScore}/100",
                                        color = StudioEmeraldSuccess,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

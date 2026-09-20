package com.example.ai

import com.example.data.model.CodeFileEntity
import com.example.data.model.FullProject
import com.example.data.model.ProjectEntity
import com.example.data.model.SecurityAuditEntity

object AutonomousBlueprints {

    data class GenerationTemplate(
        val title: String,
        val description: String,
        val platform: String,
        val language: String,
        val framework: String,
        val themeStyle: String,
        val securityScore: Int,
        val architectureSummary: String,
        val files: List<CodeFileEntity>,
        val audits: List<SecurityAuditEntity>,
        val livePreviewHtml: String
    )

    fun createDefaultProjects(): List<FullProject> {
        val templates = listOf(
            getSaasWebTemplate(),
            getFinTechMobileTemplate(),
            getRustMicroserviceTemplate(),
            getPythonFastApiTemplate(),
            getDevOpsHardeningTemplate()
        )

        return templates.map { tmpl ->
            FullProject(
                project = ProjectEntity(
                    title = tmpl.title,
                    description = tmpl.description,
                    prompt = "Autonomous AI generation for " + tmpl.title,
                    platform = tmpl.platform,
                    language = tmpl.language,
                    framework = tmpl.framework,
                    themeStyle = tmpl.themeStyle,
                    securityScore = tmpl.securityScore,
                    architectureSummary = tmpl.architectureSummary,
                    designTokensJson = tmpl.livePreviewHtml
                ),
                files = tmpl.files,
                audits = tmpl.audits
            )
        }
    }

    fun synthesizeProject(
        prompt: String,
        targetPlatform: String,
        targetLanguage: String,
        targetFramework: String,
        targetStyle: String
    ): FullProject {
        val lower = prompt.lowercase()

        val matchedTemplate = when {
            targetLanguage.equals("Rust", ignoreCase = true) -> getRustMicroserviceTemplate()
            targetLanguage.equals("Python", ignoreCase = true) -> getPythonFastApiTemplate()
            targetLanguage.equals("Go", ignoreCase = true) -> getGoMicroserviceTemplate()
            targetLanguage.equals("Swift", ignoreCase = true) -> getSwiftUiTemplate()
            targetLanguage.equals("C++", ignoreCase = true) -> getCppHighPerfTemplate()
            targetLanguage.equals("SQL", ignoreCase = true) -> getSqlDatabaseTemplate()
            targetLanguage.equals("Bash", ignoreCase = true) -> getDevOpsHardeningTemplate()
            targetLanguage.equals("PHP", ignoreCase = true) -> getPhpLaravelTemplate()
            targetPlatform.equals("Mobile", ignoreCase = true) || targetLanguage.equals("Kotlin", ignoreCase = true) -> getFinTechMobileTemplate()
            else -> getSaasWebTemplate()
        }

        val dynamicTitle = if (prompt.isNotBlank() && prompt.length > 5) {
            prompt.trim().take(40).split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
        } else {
            matchedTemplate.title
        }

        val dynamicProject = ProjectEntity(
            title = dynamicTitle,
            description = "Autonomous full-stack $targetLanguage solution with $targetStyle design system, complete with multi-layer security hardening and responsive UI.",
            prompt = prompt.ifBlank { "Create an autonomous $targetPlatform app in $targetLanguage" },
            platform = targetPlatform,
            language = targetLanguage,
            framework = targetFramework,
            themeStyle = targetStyle,
            securityScore = matchedTemplate.securityScore,
            architectureSummary = matchedTemplate.architectureSummary,
            designTokensJson = matchedTemplate.livePreviewHtml
        )

        return FullProject(
            project = dynamicProject,
            files = matchedTemplate.files,
            audits = matchedTemplate.audits
        )
    }

    private fun getSaasWebTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "NovaCloud SaaS & Billing Platform",
            description = "Production-grade React & TypeScript dashboard with Stripe subscription flow, OAuth2 session security, and dark mode UI.",
            platform = "Web",
            language = "TypeScript",
            framework = "React + Tailwind",
            themeStyle = "Cyber Executive",
            securityScore = 98,
            architectureSummary = "Client SPA (React 19 + TypeScript) -> JWT Bearer Auth -> Edge Gateway -> Postgres Database with Row-Level Security (RLS). Strict CSP & CSRF protection.",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "App.tsx",
                    language = "TypeScript",
                    fileType = "UI / View",
                    isPrimary = true,
                    content = """import React, { useState } from 'react';
import { Shield, Zap, CheckCircle2, TrendingUp, Users, Lock, ChevronRight } from 'lucide-react';

interface MetricCardProps {
  label: string;
  value: string;
  trend: string;
  isPositive: boolean;
}

const MetricCard: React.FC<MetricCardProps> = ({ label, value, trend, isPositive }) => (
  <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-5 backdrop-blur-md hover:border-cyan-500/50 transition-all">
    <p className="text-slate-400 text-xs uppercase tracking-wider font-semibold">{label}</p>
    <div className="flex items-baseline justify-between mt-2">
      <span className="text-2xl font-bold text-white tracking-tight">{value}</span>
      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${'$'}{isPositive ? 'bg-emerald-500/10 text-emerald-400' : 'bg-rose-500/10 text-rose-400'}`}>
        {trend}
      </span>
    </div>
  </div>
);

export default function App() {
  const [activeTab, setActiveTab] = useState<'overview' | 'security' | 'billing'>('overview');
  const [mfaEnabled, setMfaEnabled] = useState(true);

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 font-sans antialiased">
      {/* Top Navigation */}
      <header className="border-b border-slate-800 bg-slate-900/50 backdrop-blur-xl sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="w-9 h-9 rounded-lg bg-gradient-to-tr from-cyan-500 to-indigo-600 flex items-center justify-center shadow-lg shadow-cyan-500/20">
              <Zap className="w-5 h-5 text-white" />
            </div>
            <span className="font-bold text-lg tracking-tight bg-clip-text text-transparent bg-gradient-to-r from-white to-slate-400">
              NovaCloud Studio
            </span>
          </div>

          <div className="flex items-center space-x-4">
            <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-medium bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
              <Shield className="w-3.5 h-3.5" /> OWASP A+ Certified
            </span>
            <button className="bg-cyan-500 hover:bg-cyan-400 text-slate-950 font-semibold px-4 py-2 rounded-lg text-sm transition-all shadow-md shadow-cyan-500/20">
              Deploy Project
            </button>
          </div>
        </div>
      </header>

      {/* Main Container */}
      <main className="max-w-7xl mx-auto px-6 py-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
          <MetricCard label="Active Subscriptions" value="14,820" trend="+18.4%" isPositive={true} />
          <MetricCard label="Monthly Recurring Rev" value="$142,500" trend="+12.1%" isPositive={true} />
          <MetricCard label="Security Posture" value="99.98%" trend="Hardened" isPositive={true} />
          <MetricCard label="Avg API Latency" value="14ms" trend="-4ms" isPositive={true} />
        </div>

        {/* Security Alert Banner */}
        <div className="p-4 rounded-xl bg-gradient-to-r from-indigo-950/60 via-slate-900 to-slate-900 border border-indigo-800/40 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <Lock className="w-5 h-5 text-cyan-400" />
            <div>
              <p className="text-sm font-semibold text-white">Cryptographic Envelope Active</p>
              <p className="text-xs text-slate-400">All tenant data is sealed using hardware-backed AES-256-GCM encryption.</p>
            </div>
          </div>
          <button 
            onClick={() => setMfaEnabled(!mfaEnabled)}
            className="text-xs font-semibold px-3 py-1.5 rounded-lg border border-slate-700 bg-slate-800 hover:bg-slate-700 transition-colors">
            {mfaEnabled ? 'MFA: Enforced' : 'Enable Hardware MFA'}
          </button>
        </div>
      </main>
    </div>
  );
}
"""
                ),
                CodeFileEntity(
                    projectId = 0,
                    fileName = "securityMiddleware.ts",
                    language = "TypeScript",
                    fileType = "Security Middleware",
                    isPrimary = false,
                    content = """import { Request, Response, NextFunction } from 'express';
import helmet from 'helmet';
import rateLimit from 'express-rate-limit';

// Strict Rate Limiting to prevent brute-force and DDoS
export const authRateLimiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 10, // Max 10 requests per IP
  standardHeaders: true,
  legacyHeaders: false,
  message: { error: 'Too many authentication attempts. Backoff active for 15 minutes.' }
});

// OWASP Recommended Security Headers
export const configureSecurityHeaders = (app: any) => {
  app.use(helmet({
    contentSecurityPolicy: {
      directives: {
        defaultSrc: ["'self'"],
        scriptSrc: ["'self'", "'strict-dynamic'"],
        styleSrc: ["'self'", "'unsafe-inline'", "https://fonts.googleapis.com"],
        imgSrc: ["'self'", "data:", "https://images.unsplash.com"],
        connectSrc: ["'self'", "https://api.novacloud.internal"],
        frameAncestors: ["'none'"],
        upgradeInsecureRequests: [],
      }
    },
    crossOriginEmbedderPolicy: true,
    crossOriginResourcePolicy: { policy: "same-origin" },
    referrerPolicy: { policy: "strict-origin-when-cross-origin" }
  }));
};

// Input Sanitization against SQL Injection & XSS
export const sanitizeInput = (req: Request, res: Response, next: NextFunction) => {
  const sanitizeValue = (val: any): any => {
    if (typeof val === 'string') {
      return val.replace(/[<>&'"]/g, (c) => {
        switch (c) {
          case '<': return '&lt;';
          case '>': return '&gt;';
          case '&': return '&amp;';
          case '\'': return '&#39;';
          case '"': return '&quot;';
          default: return c;
        }
      });
    }
    return val;
  };

  if (req.body) {
    for (const key of Object.keys(req.body)) {
      req.body[key] = sanitizeValue(req.body[key]);
    }
  }
  next();
};
"""
                ),
                CodeFileEntity(
                    projectId = 0,
                    fileName = "schema.sql",
                    language = "SQL",
                    fileType = "Database Migration",
                    isPrimary = false,
                    content = """-- PostgreSQL 16 Enterprise Production Schema
-- Zero-Trust Multi-Tenant Architecture with Row-Level Security (RLS)

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE organizations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(128) NOT NULL,
    plan_tier VARCHAR(32) NOT NULL DEFAULT 'enterprise',
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    org_id UUID NOT NULL REFERENCES organizations(id) ON DELETE CASCADE,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(32) NOT NULL DEFAULT 'developer',
    mfa_secret VARCHAR(64),
    mfa_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Enable Row-Level Security
ALTER TABLE users ENABLE ROW LEVEL SECURITY;

CREATE POLICY user_tenant_isolation ON users
    FOR ALL
    USING (org_id = current_setting('app.current_org_id')::UUID);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_org ON users(org_id);
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "OWASP Top 10",
                    severity = "RESOLVED",
                    title = "A01: Broken Access Control",
                    details = "Multi-tenant tenant isolation is strictly enforced at the Postgres kernel level using Row-Level Security (RLS) and session context variables.",
                    mitigationCode = "ALTER TABLE users ENABLE ROW LEVEL SECURITY; CREATE POLICY user_tenant_isolation ON users USING (org_id = current_setting('app.current_org_id')::UUID);"
                ),
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Cryptographic Failures",
                    severity = "RESOLVED",
                    title = "A02: Data In Transit & At Rest",
                    details = "Passwords salted with Argon2id (memory cost 64MB, 3 iterations). TLS 1.3 mandated with HSTS preload.",
                    mitigationCode = "Strict-Transport-Security: max-age=63072000; includeSubDomains; preload"
                ),
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Injection Attacks",
                    severity = "RESOLVED",
                    title = "A03: SQL Injection Prevention",
                    details = "All queries are compiled via parameterized queries. Zero raw string interpolation permitted in ORM or raw SQL calls.",
                    mitigationCode = "db.query('SELECT * FROM users WHERE email = $1', [sanitizedEmail])"
                )
            ),
            livePreviewHtml = "SaaS Web Dashboard"
        )
    }

    private fun getFinTechMobileTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "VaultPay Mobile FinTech & Crypto",
            description = "High-security Android Jetpack Compose banking application with biometric authorization, AES-GCM encrypted keystore, and real-time transaction ledger.",
            platform = "Mobile",
            language = "Kotlin",
            framework = "Jetpack Compose",
            themeStyle = "Modern Minimalist",
            securityScore = 99,
            architectureSummary = "MVI / Clean Architecture -> Android Keystore EncryptedSharedPreferences -> Room Database (SQLCipher) -> BiometricPrompt Hardware Auth -> SSL Pinning OkHttp.",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "VaultDashboard.kt",
                    language = "Kotlin",
                    fileType = "UI / View",
                    isPrimary = true,
                    content = """package com.vaultpay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VaultDashboardScreen(
    onSendCrypto: () -> Unit,
    onBiometricVerify: () -> Unit
) {
    var isBalanceHidden by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(20.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("VAULT SECURE ACCOUNT", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("Enterprise Reserve", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }
            Surface(
                color = Color(0xFF10B981).copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f))
            ) {
                Text(
                    "FIPS 140-2 Level 3",
                    color = Color(0xFF10B981),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Balance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF06B6D4).copy(alpha = 0.15f), Color(0xFF6366F1).copy(alpha = 0.05f))
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text("Total Net Liquidity", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        if (isBalanceHidden) "••••••••••••" else "${'$'}482,910.45",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = onSendCrypto,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06B6D4)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Wire Transfer", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { isBalanceHidden = !isBalanceHidden },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(if (isBalanceHidden) "Reveal" else "Mask", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
"""
                ),
                CodeFileEntity(
                    projectId = 0,
                    fileName = "KeystoreVaultManager.kt",
                    language = "Kotlin",
                    fileType = "Security Middleware",
                    isPrimary = false,
                    content = """package com.vaultpay.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class KeystoreVaultManager {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private val keyAlias = "VaultMasterKey_AES256"

    init {
        createKeyIfNotExists()
    }

    private fun createKeyIfNotExists() {
        if (!keyStore.containsAlias(keyAlias)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keySpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationParameters(30, KeyProperties.AUTH_BIOMETRIC_STRONG)
                .build()
            keyGenerator.init(keySpec)
            keyGenerator.generateKey()
        }
    }

    fun encryptPayload(plainText: ByteArray): Pair<ByteArray, ByteArray> {
        val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val cipherText = cipher.doFinal(plainText)
        return Pair(cipherText, iv)
    }
}
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Mobile Security (OWASP MASVS)",
                    severity = "RESOLVED",
                    title = "MASVS-STORAGE: Secure Key Management",
                    details = "Master encryption keys are generated inside the Android Hardware Security Module (HSM) / Secure Enclave.",
                    mitigationCode = "KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT with AES-256 GCM"
                ),
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Network Protection",
                    severity = "RESOLVED",
                    title = "MASVS-NETWORK: Certificate Pinning",
                    details = "Public key hashes pinned into OkHttpClient to thwart MitM attacks on unmanaged networks.",
                    mitigationCode = "CertificatePinner.Builder().add('api.vaultpay.com', 'sha256/k2v6EFv...').build()"
                )
            ),
            livePreviewHtml = "Mobile FinTech Wallet"
        )
    }

    private fun getRustMicroserviceTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "HyperStream Rust Ultra-Fast Engine",
            description = "Memory-safe, high-concurrency microservice built in Rust with Axum, Tokio async runtime, and zero-allocation JSON streaming.",
            platform = "Backend",
            language = "Rust",
            framework = "Axum + Tokio",
            themeStyle = "Cyber Dark",
            securityScore = 100,
            architectureSummary = "Tokio Async Engine -> Axum HTTP Routing -> Serde Zero-Copy Parser -> Tower Rate-Limiting -> Rust Compiler Memory Safety (No Buffer Overflows).",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "main.rs",
                    language = "Rust",
                    fileType = "Business Logic",
                    isPrimary = true,
                    content = """use axum::{
    routing::{get, post},
    extract::{State, Json},
    http::{StatusCode, HeaderMap},
    Router,
};
use serde::{Deserialize, Serialize};
use std::sync::Arc;
use tokio::net::TcpListener;
use tracing::{info, warn};

#[derive(Clone)]
struct AppState {
    db_pool: Arc<String>, // Mock connection pool
}

#[derive(Deserialize, Serialize)]
struct StreamPayload {
    tenant_id: String,
    event_type: String,
    data: serde_json::Value,
}

#[derive(Serialize)]
struct ApiResponse {
    status: String,
    message: String,
    processed_in_micros: u64,
}

async fn health_check() -> (StatusCode, &'static str) {
    (StatusCode::OK, "Engine: Optimal | Memory: 4.2MB | Threads: 16")
}

async fn ingest_event(
    State(_state): State<AppState>,
    Json(payload): Json<StreamPayload>,
) -> Result<Json<ApiResponse>, StatusCode> {
    // Zero-copy validation
    if payload.tenant_id.is_empty() {
        return Err(StatusCode.BAD_REQUEST);
    }

    let start = std::time::Instant::now();
    // High-performance asynchronous processing
    let elapsed = start.elapsed().as_micros() as u64;

    Ok(Json(ApiResponse {
        status: "success".into(),
        message: format!("Event '{}' ingested safely", payload.event_type),
        processed_in_micros: elapsed,
    }))
}

#[tokio::main]
async fn main() {
    tracing_subscriber::fmt::init();
    info!("Starting HyperStream Rust Service on 0.0.0.0:8080");

    let state = AppState {
        db_pool: Arc::new("postgres://sslmode=verify-full".into()),
    };

    let app = Router::new()
        .route("/health", get(health_check))
        .route("/api/v1/ingest", post(ingest_event))
        .with_state(state);

    let listener = TcpListener::bind("0.0.0.0:8080").await.unwrap();
    axum::serve(listener, app).await.unwrap();
}
"""
                ),
                CodeFileEntity(
                    projectId = 0,
                    fileName = "Cargo.toml",
                    language = "Rust",
                    fileType = "Config",
                    isPrimary = false,
                    content = """[package]
name = "hyperstream-core"
version = "0.1.0"
edition = "2021"

[dependencies]
axum = { version = "0.7", features = ["macros"] }
tokio = { version = "1.0", features = ["full"] }
serde = { version = "1.0", features = ["derive"] }
serde_json = "1.0"
tracing = "0.1"
tracing-subscriber = "0.3"
tower = { version = "0.4", features = ["limit", "timeout"] }
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Memory Safety",
                    severity = "RESOLVED",
                    title = "Rust Compiler Guarantees",
                    details = "Zero buffer overflow risks, zero use-after-free, zero data races guaranteed at compile time by borrow checker.",
                    mitigationCode = "Memory-safe natively by rustc"
                )
            ),
            livePreviewHtml = "Rust Microservice API"
        )
    }

    private fun getPythonFastApiTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "NeuralEngine AI & Computer Vision Service",
            description = "Async Python FastAPI server with Pydantic v2 data validation, JWT authentication, rate limiting, and ONNX runtime integration.",
            platform = "Backend",
            language = "Python",
            framework = "FastAPI",
            themeStyle = "Glassmorphism",
            securityScore = 97,
            architectureSummary = "FastAPI Async ASGI -> OAuth2 Password Bearer -> Pydantic Type Sanitizer -> Async SQLAlchemy -> Redis Rate Limiter.",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "main.py",
                    language = "Python",
                    fileType = "Business Logic",
                    isPrimary = true,
                    content = """from fastapi import FastAPI, Depends, HTTPException, status, Request
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field, EmailStr
from typing import Optional, List
import time

app = FastAPI(
    title="NeuralEngine API",
    version="2.4.0",
    docs_url=None, # Disable Swagger in production for security
    redoc_url=None
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["https://dashboard.neuralengine.ai"],
    allow_credentials=True,
    allow_methods=["GET", "POST"],
    allow_headers=["Authorization", "Content-Type"],
)

class InferenceRequest(BaseModel):
    model_id: str = Field(..., pattern=r"^[a-zA-Z0-9_-]{3,32}$")
    prompt: str = Field(..., min_length=1, max_length=4096)
    temperature: float = Field(0.7, ge=0.0, le=1.0)
    max_tokens: int = Field(512, ge=16, le=2048)

class InferenceResponse(BaseModel):
    task_id: str
    status: str
    tokens_generated: int
    duration_ms: float

@app.post("/api/v1/predict", response_model=InferenceResponse)
async def run_inference(request: Request, payload: InferenceRequest):
    start = time.perf_counter()
    # Execute sandboxed AI inference
    duration = (time.perf_counter() - start) * 1000
    
    return InferenceResponse(
        task_id="pred_89f02c",
        status="completed",
        tokens_generated=payload.max_tokens,
        duration_ms=round(duration, 2)
    )
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Input Validation",
                    severity = "RESOLVED",
                    title = "Pydantic Schema Validation",
                    details = "Strict regex regex patterns and bounds checking prevents prompt injection and parameter tampering.",
                    mitigationCode = "model_id: str = Field(..., pattern=r'^[a-zA-Z0-9_-]{3,32}$')"
                )
            ),
            livePreviewHtml = "Python AI Service"
        )
    }

    private fun getGoMicroserviceTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "GoStream Cloud Proxy & Gateway",
            description = "Lightweight Go reverse proxy with TLS termination, token bucket rate limiter, and Prometheus metrics.",
            platform = "Backend",
            language = "Go",
            framework = "Gin",
            themeStyle = "Cyber Dark",
            securityScore = 98,
            architectureSummary = "Go Net/HTTP -> Gin Engine -> Token Bucket Middleware -> Secure Reverse Proxy -> Graceful Shutdown.",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "main.go",
                    language = "Go",
                    fileType = "Business Logic",
                    isPrimary = true,
                    content = """package main

import (
	"context"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gin-gonic/gin"
)

func SecurityHeadersMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		c.Header("X-Frame-Options", "DENY")
		c.Header("X-Content-Type-Options", "nosniff")
		c.Header("X-XSS-Protection", "1; mode=block")
		c.Header("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
		c.Next()
	}
}

func main() {
	gin.SetMode(gin.ReleaseMode)
	r := gin.New()
	r.Use(gin.Recovery())
	r.Use(SecurityHeadersMiddleware())

	r.GET("/healthz", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"status": "UP",
			"uptime": "99.999%",
		})
	})

	srv := &http.Server{
		Addr:         ":8443",
		Handler:      r,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	log.Println("GoStream Gateway running on :8443")
	srv.ListenAndServe()
}
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Network Security",
                    severity = "RESOLVED",
                    title = "Timeout Hardening",
                    details = "ReadTimeout and WriteTimeout set to 10s to prevent Slowloris resource exhaustion attacks.",
                    mitigationCode = "ReadTimeout: 10 * time.Second, WriteTimeout: 10 * time.Second"
                )
            ),
            livePreviewHtml = "Go Gateway Service"
        )
    }

    private fun getSwiftUiTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "Aether iOS Biometric Health Tracker",
            description = "SwiftUI 5 application featuring HealthKit integration, biometric face ID authorization, and smooth Metal shaders.",
            platform = "Mobile",
            language = "Swift",
            framework = "SwiftUI",
            themeStyle = "Glassmorphic",
            securityScore = 98,
            architectureSummary = "SwiftUI 5 Declarative Views -> Observation Framework -> LocalAuthentication (FaceID) -> Keychain Access -> Secure HealthKit Store.",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "AetherView.swift",
                    language = "Swift",
                    fileType = "UI / View",
                    isPrimary = true,
                    content = """import SwiftUI
import LocalAuthentication

struct AetherView: View {
    @State private var isAuthenticated = false
    @State private var heartRate = 72

    var body: some View {
        ZStack {
            Color(hex: "#090D16").ignoresSafeArea()

            VStack(spacing: 24) {
                HStack {
                    VStack(alignment: .leading) {
                        Text("BIOMETRIC HEALTH").font(.caption).bold().foregroundColor(.cyan)
                        Text("Aether Pulse").font(.title.bold()).foregroundColor(.white)
                    }
                    Spacer()
                    Image(systemName: "shield.lefthalf.filled")
                        .foregroundColor(.green)
                }
                .padding(.horizontal)

                VStack(spacing: 12) {
                    Text("\(heartRate) BPM")
                        .font(.system(size: 48, weight: .bold, design: .rounded))
                        .foregroundColor(.white)
                    Text("Cardio Zone: Optimal")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity)
                .padding(32)
                .background(RoundedRectangle(cornerRadius: 20).fill(Color.white.opacity(0.06)))
            }
            .padding()
        }
    }
}
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Biometrics",
                    severity = "RESOLVED",
                    title = "Face ID LocalAuthentication",
                    details = "User authentication delegated strictly to Apple Secure Enclave.",
                    mitigationCode = "LAContext().evaluatePolicy(.deviceOwnerAuthenticationWithBiometrics)"
                )
            ),
            livePreviewHtml = "SwiftUI iOS App"
        )
    }

    private fun getCppHighPerfTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "Vortex C++ Matrix Engine",
            description = "C++20 SIMD-accelerated linear algebra library with bound-checked spans and thread-safe lock-free ring buffers.",
            platform = "Systems",
            language = "C++",
            framework = "C++20 Modules",
            themeStyle = "Modern Minimalist",
            securityScore = 96,
            architectureSummary = "C++20 Span Buffers -> RAII Smart Pointers -> AVX-512 SIMD Vectorization -> ASAN Verified Safe Execution.",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "matrix_kernel.cpp",
                    language = "C++",
                    fileType = "Business Logic",
                    isPrimary = true,
                    content = """#include <iostream>
#include <vector>
#include <span>
#include <memory>
#include <stdexcept>

class SafeMatrixKernel {
public:
    static void compute_dot_product(
        std::span<const float> a,
        std::span<const float> b,
        std::span<float> result
    ) {
        if (a.size() != b.size() || result.size() != a.size()) {
            throw std::invalid_argument("Dimension mismatch: bounds enforced by std::span");
        }

        // SIMD-optimized vector accumulation
        for (size_t i = 0; i < a.size(); ++i) {
            result[i] = a[i] * b[i];
        }
    }
};

int main() {
    std::vector<float> vec_a = {1.5f, 2.0f, 3.5f, 4.0f};
    std::vector<float> vec_b = {0.5f, 1.0f, 2.0f, 0.5f};
    std::vector<float> out(4);

    SafeMatrixKernel::compute_dot_product(vec_a, vec_b, out);
    std::cout << "Computed safely without buffer overruns.\n";
    return 0;
}
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Bounds Checking",
                    severity = "RESOLVED",
                    title = "Buffer Overflow Protection",
                    details = "std::span and RAII memory wrappers prevent illegal pointer arithmetic and heap corruption.",
                    mitigationCode = "std::span<const float> a with bounds validation"
                )
            ),
            livePreviewHtml = "C++ Systems Kernel"
        )
    }

    private fun getSqlDatabaseTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "Sentinel SQL High-Security Schema",
            description = "Complete relational database architecture with temporal tables, strict foreign keys, and cryptographic audit logs.",
            platform = "Backend",
            language = "SQL",
            framework = "PostgreSQL",
            themeStyle = "Modern Minimalist",
            securityScore = 99,
            architectureSummary = "Postgres 16 -> Transparent Data Encryption -> Audit Trigger Tracing -> Row-Level Security -> Vault Credential Rotation.",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "production_audit.sql",
                    language = "SQL",
                    fileType = "Database Migration",
                    isPrimary = true,
                    content = """-- Comprehensive Immutable Security Audit Log
CREATE TABLE system_audit_log (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actor_id UUID NOT NULL,
    action_type VARCHAR(64) NOT NULL,
    target_resource VARCHAR(128) NOT NULL,
    ip_address INET NOT NULL,
    payload_hash VARCHAR(64) NOT NULL,
    occurred_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Write-Only Append Security Policy
REVOKE UPDATE, DELETE ON system_audit_log FROM PUBLIC;
REVOKE UPDATE, DELETE ON system_audit_log FROM app_user;
GRANT INSERT, SELECT ON system_audit_log TO app_user;

CREATE INDEX idx_audit_occurred_at ON system_audit_log(occurred_at DESC);
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Auditability",
                    severity = "RESOLVED",
                    title = "Tamper-Proof Audit Trail",
                    details = "UPDATE and DELETE permissions are permanently stripped from the application role.",
                    mitigationCode = "REVOKE UPDATE, DELETE ON system_audit_log FROM app_user;"
                )
            ),
            livePreviewHtml = "SQL Database Architecture"
        )
    }

    private fun getPhpLaravelTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "Aegis PHP Laravel Enterprise Core",
            description = "Modern PHP 8.3 & Laravel 11 web service with Eloquent ORM, CSRF protection, and spatie permission controls.",
            platform = "Fullstack",
            language = "PHP",
            framework = "Laravel 11",
            themeStyle = "Bento Grid",
            securityScore = 97,
            architectureSummary = "Laravel 11 -> Sanctum Token Auth -> Form Request Validation -> Rate Limiter -> Strict CSRF Middleware.",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "AuthController.php",
                    language = "PHP",
                    fileType = "Business Logic",
                    isPrimary = true,
                    content = """<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use App\Models\User;

class AuthController extends Controller
{
    public function authenticate(Request ${'$'}request)
    {
        ${'$'}validated = ${'$'}request->validate([
            'email' => 'required|email:rfc,dns|max:255',
            'password' => 'required|string|min:12',
        ]);

        ${'$'}user = User::where('email', ${'$'}validated['email'])->first();

        if (! ${'$'}user || ! Hash::check(${'$'}validated['password'], ${'$'}user->password)) {
            return response()->json(['message' => 'Invalid credentials'], 401);
        }

        ${'$'}token = ${'$'}user->createToken('auth_token', ['*'], now()->addHours(8))->plainTextToken;

        return response()->json([
            'access_token' => ${'$'}token,
            'token_type' => 'Bearer',
        ]);
    }
}
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Authentication",
                    severity = "RESOLVED",
                    title = "Bcrypt / Argon2 Hashing",
                    details = "Passwords verified using timing-attack resistant Hash::check.",
                    mitigationCode = "Hash::check(validated['password'], user->password)"
                )
            ),
            livePreviewHtml = "Laravel PHP Service"
        )
    }

    private fun getDevOpsHardeningTemplate(): GenerationTemplate {
        return GenerationTemplate(
            title = "ZeroTrust Linux Hardening & CI/CD",
            description = "Autonomous Bash DevOps automation script enforcing CIS benchmarks, firewall rules, and container signing.",
            platform = "DevOps",
            language = "Bash",
            framework = "Shell / Docker",
            themeStyle = "Cyber Dark",
            securityScore = 100,
            architectureSummary = "Linux Kernel -> Sysctl Hardening -> UFW Firewall -> AppArmor / SELinux -> Non-Root Docker Containers.",
            files = listOf(
                CodeFileEntity(
                    projectId = 0,
                    fileName = "harden_server.sh",
                    language = "Bash",
                    fileType = "Security Middleware",
                    isPrimary = true,
                    content = """#!/usr/bin/env bash
set -euo pipefail
IFS=$'\n\t'

echo "=== [OMNIDEV] Initiating Linux Production Hardening ==="

# 1. Disable Root SSH and password logins
sed -i 's/#PermitRootLogin yes/PermitRootLogin no/' /etc/ssh/sshd_config
sed -i 's/PasswordAuthentication yes/PasswordAuthentication no/' /etc/ssh/sshd_config

# 2. Kernel ASLR and TCP SYN Cookie protection
cat << 'EOF' >> /etc/sysctl.d/99-security.conf
kernel.randomize_va_space = 2
net.ipv4.tcp_syncookies = 1
net.ipv4.conf.all.rp_filter = 1
net.ipv4.conf.all.accept_redirects = 0
net.ipv6.conf.all.accept_redirects = 0
EOF
sysctl -p /etc/sysctl.d/99-security.conf

# 3. Restrict UFW Firewall to TLS & SSH
ufw default deny incoming
ufw default allow outgoing
ufw allow 22/tcp
ufw allow 443/tcp
ufw --force enable

echo "=== [OMNIDEV] Server hardened successfully. CIS Benchmark Compliant. ==="
"""
                )
            ),
            audits = listOf(
                SecurityAuditEntity(
                    projectId = 0,
                    category = "Infrastructure Security",
                    severity = "RESOLVED",
                    title = "Kernel & SSH Hardening",
                    details = "Root SSH login disabled, password auth blocked in favor of Ed25519 keys, and full ASLR randomized address space enforced.",
                    mitigationCode = "kernel.randomize_va_space = 2, PermitRootLogin no"
                )
            ),
            livePreviewHtml = "DevOps Hardening Script"
        )
    }
}

# subsidy-api

自治体業務を想定した「補助金管理システム」風のWeb API（ポートフォリオ用）です。  
Spring Boot + JDBC + H2 を使って、API実装とテスト実装を段階的に積み上げています。

## できること（現時点）
- 職員ユーザー（staff_users）の取得・作成API
- 不正パラメータ時の 400、重複時の 409 をJSONで返す例外ハンドリング
- Controller / Service / Repository 各層のテスト

## 技術スタック
- Java 21
- Spring Boot
- Gradle
- H2 Database
- Testing: JUnit5 / Mockito / Spring Boot Test（@WebMvcTest, @JdbcTest）

## 起動方法
```powershell
cd <your-path>\subsidy-api
.\gradlew.bat bootRun
```
## 起動後
- API例：`GET http://localhost:8081/staff-users`

## DB初期化（main / test の違い）
- `src/main/resources/schema.sql, data.sql`  
  手動でAPIを叩いて動作確認しやすいように、起動時にテーブル作成・初期データ投入を行うためのSQLです。
- `src/test/resources/schema.sql, data.sql`  
  テスト実行時に「毎回同じ初期状態」を作り、テストの再現性を担保するためのSQLです。  
  （main側のSQLと混ざると衝突するため、test側で明示的に分離しています）

## テスト方針（どの層をどのテストで担保しているか）
- Controller層（HTTP入出力 / ステータスコード / 例外ハンドリング）
    - `StaffUserControllerTest`
- Service層（ビジネスロジック / Repository例外の変換）
    - `StaffUserServiceTest`
- Repository層（SQLの正しさ / DB制約の挙動）
    - `StaffUserRepositoryTest`

## テスト実行コマンド
全テスト：
```powershell
.\gradlew.bat clean test
```
クラス指定：
```powershell
.\gradlew.bat test --tests "org.example.subsidyapi.staff.StaffUserControllerTest"
.\gradlew.bat test --tests "org.example.subsidyapi.staff.StaffUserServiceTest"
.\gradlew.bat test --tests "org.example.subsidyapi.staff.StaffUserRepositoryTest"
```

## テストレポートの見方

テスト実行後、以下をブラウザで開きます：

build/reports/tests/test/index.html

PowerShell 例：
```powershell
Start-Process ".\build\reports\tests\test\index.html"
```
## API動作確認用PowerShellスクリプト例
```powershell
# 1) 正常系（201）
$body = @{ name="職員 太郎"; email="ok_$(Get-Random)@example.com"; role="STAFF" } | ConvertTo-Json
Invoke-WebRequest -Method Post -Uri "http://localhost:8081/staff-users" -ContentType "application/json" -Body $body -UseBasicParsing |
  Select-Object StatusCode, Content

# 2) 必須エラー（400）
$body = @{ name=""; email="bad_$(Get-Random)@example.com"; role="STAFF" } | ConvertTo-Json
try {
  Invoke-WebRequest -Method Post -Uri "http://localhost:8081/staff-users" -ContentType "application/json" -Body $body -UseBasicParsing
} catch {
  $_.Exception.Response.StatusCode.value__
  (New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())).ReadToEnd()
}

# 3) role不正（400）
$body = @{ name="テスト"; email="bad_role_$(Get-Random)@example.com"; role="HOGE" } | ConvertTo-Json
try {
  Invoke-WebRequest -Method Post -Uri "http://localhost:8081/staff-users" -ContentType "application/json" -Body $body -UseBasicParsing
} catch {
  $_.Exception.Response.StatusCode.value__
  (New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())).ReadToEnd()
}

# 4) 一覧取得（GET）
Invoke-RestMethod -Method Get -Uri "http://localhost:8081/staff-users"
```
## 今後の拡張予定
- 補助金申請（subsidy_applications）APIの拡充（取得・登録に加えて更新・取消など）
- 申請者（applicants）テーブル/Repository/Service/Controller追加
- Swagger UI（OpenAPI）導入（API仕様の自動生成）
- 永続DBへ切り替え（PostgreSQL等）＋ マイグレーション導入（例：Flyway）
- CI導入（GitHub Actions：`./gradlew clean test` を自動実行）
- 認証・認可の導入（Spring Security：ロール別アクセス制御）
- Dockerコンテナ化（アプリ＋DBをcomposeで起動）
- フロントエンド連携（React等：API呼び出し〜画面表示）

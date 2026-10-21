param(
  [string]$HostName = "localhost",
  [int]$Port = 3306,
  [string]$UserName = "interview_tracker",
  [string]$Schema = "interview_tracker"
)

$ErrorActionPreference = "Stop"
$connectionFile = Join-Path $env:APPDATA "MySQL\Workbench\connections.xml"

if (-not (Test-Path -LiteralPath $connectionFile)) {
  throw "MySQL Workbench settings were not found at $connectionFile. Open Workbench once, then run this script again."
}

[xml]$connections = Get-Content -LiteralPath $connectionFile -Raw
$list = $connections.SelectSingleNode("/data/value")
$template = $list.SelectSingleNode("value[link[@key='driver']='com.mysql.rdbms.mysql.driver.native']")
if ($null -eq $template) {
  throw "No standard TCP/IP MySQL connection was found to use as a Workbench profile template."
}

$profileName = "Interview Tracker (Local)"
$existing = $list.SelectSingleNode("value[value[@key='name']='$profileName']")
if ($null -ne $existing) {
  $list.RemoveChild($existing) | Out-Null
}

$profile = $template.CloneNode($true)
$profile.SetAttribute("id", "{" + [guid]::NewGuid().ToString().ToUpperInvariant() + "}")
foreach ($node in @($profile) + @($profile.SelectNodes(".//*[@_ptr_]"))) {
  if ($node.HasAttribute("_ptr_")) {
    $node.SetAttribute("_ptr_", [guid]::NewGuid().ToString("N").Substring(0, 16).ToUpperInvariant())
  }
}
$profile.SelectSingleNode("value[@key='name']").InnerText = $profileName
$profile.SelectSingleNode("value[@key='hostIdentifier']").InnerText = "Mysql@$HostName`:$Port"
$profile.SelectSingleNode("value[@key='isDefault']").InnerText = "0"

$parameters = $profile.SelectSingleNode("value[@key='parameterValues']")
$parameters.SelectSingleNode("value[@key='hostName']").InnerText = $HostName
$parameters.SelectSingleNode("value[@key='port']").InnerText = [string]$Port
$parameters.SelectSingleNode("value[@key='userName']").InnerText = $UserName
$parameters.SelectSingleNode("value[@key='schema']").InnerText = $Schema
$password = $parameters.SelectSingleNode("value[@key='password']")
if ($null -ne $password) { $password.InnerText = "" }
$lastSchema = $parameters.SelectSingleNode("value[@key='DbSqlEditor:LastDefaultSchema']")
if ($null -ne $lastSchema) { $lastSchema.InnerText = $Schema }
$lastConnected = $parameters.SelectSingleNode("value[@key='lastConnected']")
if ($null -ne $lastConnected) { $lastConnected.InnerText = "0" }

$list.AppendChild($profile) | Out-Null
$connections.Save($connectionFile)
Write-Output "Added '$profileName' to MySQL Workbench. Password storage is blank; Workbench will prompt when you connect."

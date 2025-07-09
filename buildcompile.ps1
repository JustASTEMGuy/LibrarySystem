Write-Host "Compiling Java Source Files..."

# Get all .java files in src/ recursively
$files = Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName }

# Compile to bin/ with classpath to lib/*
javac --release 21 -d bin -cp "lib/*" $files

if ($?) {
    Write-Host "Build complete!"
} else {
    Write-Host "Nah, build failed..."
}
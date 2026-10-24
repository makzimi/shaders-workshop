#!/usr/bin/env python3
"""Copy matching End files into a disposable Start project and compile it."""
from pathlib import Path
import argparse, hashlib, os, shutil, subprocess, tempfile

root = Path(__file__).resolve().parents[1]
source_path = Path('app/src/main/java/dev/maxkach/shaderworkshop')
parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('--device')
args = parser.parse_args()
def hashes():
    return {str(p.relative_to(root)): hashlib.sha256(p.read_bytes()).hexdigest()
            for p in (root / source_path).rglob('*.kt')}
before = hashes()
with tempfile.TemporaryDirectory(prefix='shader-workshop-recovery-') as t:
    copy = Path(t) / 'project'
    shutil.copytree(root, copy, ignore=shutil.ignore_patterns('build', '.gradle', '.kotlin', 'captures', '__pycache__', 'local.properties'))
    for exercise, files in {
        'exercise1': ['SpotlightShader.kt', 'SpotlightEffect.kt'],
        'exercise2': ['grading/GradingShader.kt', 'glitch/GlitchShader.kt', 'vines/VinesShader.kt', 'WorkshopEffect.kt'],
    }.items():
        for name in files:
            body = (copy / source_path / exercise / 'end' / name).read_text()
            (copy / source_path / exercise / 'start' / name).write_text(body.replace(f'{exercise}.end', f'{exercise}.start'))
    subprocess.run([str(copy / 'gradlew'), '-p', str(copy), ':app:compileDebugKotlin'], check=True)
    if args.device:
        subprocess.run([str(copy / 'gradlew'), '-p', str(copy), ':app:connectedDebugAndroidTest',
            '-Pandroid.testInstrumentationRunnerArguments.class=dev.maxkach.shaderworkshop.UncommentedScreenTest',
            '-Pandroid.testInstrumentationRunnerArguments.uncommented=true'],
            env=dict(os.environ, ANDROID_SERIAL=args.device), check=True)
assert before == hashes(), 'Recovery modified the working exercise files'
print('PASS: matching End answers compile in Start; original source unchanged')

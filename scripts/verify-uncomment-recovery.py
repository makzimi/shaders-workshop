#!/usr/bin/env python3
"""Build an actual uncommented Start copy; optionally test its complete screens on a device."""
import argparse
import hashlib
import json
from pathlib import Path
import shutil
import subprocess
import tempfile
from exercise_steps import complete_starter

ROOT = Path(__file__).resolve().parents[1]

def hashes():
    return {str(p.relative_to(ROOT)): hashlib.sha256(p.read_bytes()).hexdigest()
            for p in (ROOT / 'app/src').rglob('*') if p.is_file()}

def verify(output, device):
    if output.exists() or output == ROOT or ROOT in output.parents or output in ROOT.parents:
        raise ValueError('Choose a fresh output directory outside the source project')
    before = hashes()
    shutil.copytree(ROOT, output, ignore=shutil.ignore_patterns('build', '.gradle', '.kotlin', 'captures', '__pycache__', 'local.properties'))
    steps = complete_starter(output)
    command = [str(output / 'gradlew'), '-p', str(output), ':app:assembleDebug', ':app:assembleDebugAndroidTest']
    subprocess.run(command, check=True)
    if device:
        import os
        env = dict(os.environ, ANDROID_SERIAL=device)
        subprocess.run([str(output / 'gradlew'), '-p', str(output), ':app:connectedDebugAndroidTest',
                        '-Pandroid.testInstrumentationRunnerArguments.class=dev.maxkach.shaderworkshop.UncommentedScreenTest',
                        '-Pandroid.testInstrumentationRunnerArguments.uncommented=true'], env=env, check=True)
    assert before == hashes(), 'Original workshop source changed during recovery'
    manifest = {'steps': steps, 'sourceHashes': before, 'completedSourceHashes': {
        name: hashlib.sha256((output / 'app/src/main/java/dev/maxkach/shaderworkshop' / name).read_bytes()).hexdigest()
        for name in steps}, 'apkSha256': hashlib.sha256((output / 'app/build/outputs/apk/debug/app-debug.apk').read_bytes()).hexdigest(),
        'device': device, 'screenTests': 'pass' if device else 'not run'}
    (output / 'uncomment-verification.json').write_text(json.dumps(manifest, indent=2) + '\n')
    print('PASS: actual comment removal builds both independent Start exercises; original source unchanged')

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path)
    parser.add_argument('--device')
    args = parser.parse_args()
    if args.output:
        verify(args.output.resolve(), args.device)
    else:
        with tempfile.TemporaryDirectory(prefix='workshop-uncomment-') as directory:
            verify(Path(directory) / 'project', args.device)

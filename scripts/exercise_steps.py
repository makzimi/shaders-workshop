"""Remove only the block-comment delimiters under numbered live-coding cues."""
import re
from pathlib import Path

STEP = re.compile(r'(?m)(// STEP-BEGIN ([\w-]+):[^\n]+\n)[ \t]*/\*\n([\s\S]*?)\n[ \t]*\*/')
EXERCISE_FILES = (
    'exercise1/start/SpotlightEffect.kt', 'exercise1/start/SpotlightShader.kt',
    'exercise2/start/grading/GradingShader.kt', 'exercise2/start/glitch/GlitchShader.kt',
    'exercise2/start/vines/VinesShader.kt', 'exercise2/start/WorkshopEffect.kt',
)

def uncomment(source):
    steps = list(STEP.finditer(source))
    if not steps:
        raise ValueError('No commented workshop steps found')
    return STEP.sub(lambda m: m[1] + m[3], source), [m[2] for m in steps]

def complete_starter(project):
    source = Path(project) / 'app/src/main/java/dev/maxkach/shaderworkshop'
    completed = {}
    for name in EXERCISE_FILES:
        file = source / name
        code, ids = uncomment(file.read_text())
        file.write_text(code)
        completed[name] = ids
    return completed

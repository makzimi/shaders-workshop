"""Recovery must remove delimiters, never edit or substitute the exercise implementation."""
import unittest
from exercise_steps import uncomment

class UncommentTest(unittest.TestCase):
    def test_only_delimiters_are_removed(self):
        source = '// explanation\n// STEP-BEGIN light-1: Compute distance.\n/*\nfloat d = length(p);\n*/\nreturn c;\n'
        actual, ids = uncomment(source)
        self.assertEqual(actual, '// explanation\n// STEP-BEGIN light-1: Compute distance.\nfloat d = length(p);\nreturn c;\n')
        self.assertEqual(ids, ['light-1'])

    def test_missing_and_unclosed_steps_are_rejected(self):
        for source in ('return c;', '// STEP-BEGIN a-1: Test.\n/*\nreturn c;' ):
            with self.assertRaises(ValueError):
                uncomment(source)

if __name__ == '__main__':
    unittest.main()

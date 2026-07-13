import sys
import playwright


def test_environment():
    print(sys.executable)
    print(playwright.__file__)
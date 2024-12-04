import pytest
import unittest

from modules.sfp_fortinet import sfp_fortinet
from sflib import SpiderFoot


@pytest.mark.usefixtures
class TestModuleFortinet(unittest.TestCase):

    def test_opts(self):
        module = sfp_fortinet()
        self.assertEqual(len(module.opts), len(module.optdescs))

    def test_setup(self):
        sf = SpiderFoot(self.default_options)
        module = sfp_fortinet()
        module.setup(sf, dict())

    def test_watchedEvents_should_return_list(self):
        module = sfp_fortinet()
        self.assertIsInstance(module.watchedEvents(), list)

    def test_producedEvents_should_return_list(self):
        module = sfp_fortinet()
        self.assertIsInstance(module.producedEvents(), list)
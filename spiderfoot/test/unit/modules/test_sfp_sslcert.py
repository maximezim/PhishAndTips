import pytest
import unittest

from modules.sfp_sslcert import sfp_sslcert
from sflib import SpiderFoot


@pytest.mark.usefixtures
class TestModuleSslCert(unittest.TestCase):

    def test_opts(self):
        module = sfp_sslcert()
        self.assertEqual(len(module.opts), len(module.optdescs))

    def test_setup(self):
        sf = SpiderFoot(self.default_options)
        module = sfp_sslcert()
        module.setup(sf, dict())

    def test_watchedEvents_should_return_list(self):
        module = sfp_sslcert()
        self.assertIsInstance(module.watchedEvents(), list)

    def test_producedEvents_should_return_list(self):
        module = sfp_sslcert()
        self.assertIsInstance(module.producedEvents(), list)

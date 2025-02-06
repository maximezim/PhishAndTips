# PhishAndTips

## ENV Variables:
```bash
#################################################
# ***** Authentication Database Credentials *****
#################################################

# Authentication DB User and Password are required
AUTH_PG_USER=
AUTH_PG_PASSWORD=

##################################################
# ******** Formation Database Credentials ********
##################################################

# Formation DB User and Password are required
FORMATION_PG_USER=
FORMATION_PG_PASSWORD=

##################################################
# ********** SMTP Server Configuration ***********
##################################################

# SMTP Username and Password are required
SMTP_MAIL_USERNAME=
SMTP_MAIL_PASSWORD=

# SMTP Host, if not provided, mailserver will be used
SMTP_HOST=

# SMTP Port, if not provided, 25 will be used
SMTP_PORT=

# Email from address, if not provided, sender@example.com will be used
SMTP_FROM=

# Enable TLS, if not provided, false will be used
SMTP_ENABLE_TLS=

##################################################
# *************** S3 Configuration ***************
##################################################

# Access Key and Secret Key are required
S3_ACCESS_KEY=
S3_SECRET_KEY=

# Endpoint is optional, if not provided, http://localhost:8080 will be used
S3_ENDPOINT=

# Bucket is optional, if not provided, 'assets' will be used
S3_BUCKET=

# Use local or external S3 server, if not provided, local will be used
S3_USE_LOCAL=

##################################################
# ************ GoPhish Configuration *************
##################################################

# GoPhish API Key is required
GOPHISH_API_KEY=
```
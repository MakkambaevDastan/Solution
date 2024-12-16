@echo off
echo.
echo Generating root CA and certificate.
openssl req -new -newkey rsa:4096 -nodes -out milbursamuRA.com.csr -keyout milbursamuRA.com.key -subj "/C=US/ST=State/L=City/O=Organization/OU=Department/CN=milbursamuRA.com"
openssl x509 -trustout -sha256 -signkey milbursamuRA.com.key -days 365 -req -in milbursamuRA.com.csr -out milbursamuRA.com.pem
echo.
echo Generating frontend certificate.
openssl genrsa -out frontend.org.key 2048
openssl req -new -key frontend.org.key -out frontend.org.csr -subj "/C=US/ST=State/L=City/O=FrontendOrg/OU=Frontend/CN=frontend.org"
echo.
echo Signing frontend certificate.
openssl x509 -req -days 365 -in frontend.org.csr -CA milbursamuRA.com.pem -CAkey milbursamuRA.com.key -set_serial 01 -out frontend.org.pem
echo Certificates generated successfully.
pause

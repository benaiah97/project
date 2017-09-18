CERTS=$(echo -n | openssl s_client -connect iago-latest.apac.wdpr.disney.com:443 -showcerts | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p')
echo "$CERTS" | awk -v RS="-----BEGIN CERTIFICATE-----" 'NR > 1 { printf RS $0 > "'$SERVER_ROOT_CERTIFICATE'"; close("'$SERVER_ROOT_CERTIFICATE'") }'

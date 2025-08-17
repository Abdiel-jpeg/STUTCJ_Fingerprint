import os
from base64 import b64encode

#Generate AES-128 bits random key. Since 16 bytes = 128 bits
random_key = os.urandom(16)

#Encode byte array to utf-8 string
b64_random_key = b64encode(random_key).decode("utf-8")

print("Use this key in env variables for fingerprint encryption")
print(b64_random_key)
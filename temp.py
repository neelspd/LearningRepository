# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

import psycopg2

connection = psycopg2.connect(database = "postgres", user = "postgres",
                                  password = "04AprilSU",
                                  host = "141.13.162.158",
                                  port = "5432")

print("Opened database successfully")

cursor = connection.cursor()

cursor.execute("select message_body from email.emails")
rows = cursor.fetchall()
for row in rows:
   print(row[0] + "\n")
   WordList = str(row[0]).split()
   word_length = len(WordList);
   print("Length: " +  str(word_length) + " \n")
   for word in WordList:
       frequency = str(row[0]).count(word)
       print( word + " : " + str(frequency))
       print("\n")
   print("\n\n")

print("Operation done successfully")

cursor.close()
connection.close()
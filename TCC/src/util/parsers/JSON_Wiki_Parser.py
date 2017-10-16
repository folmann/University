import json
import sys
import psycopg2

path = "C:\\Users\\lucas\\Desktop\\Lucas\\Faculdade\\TCC\\WikiDump\\latest-all.json"
with open(path) as file:
	line = file.readline()
	for line in file:
		#page = json.loads(line)
		page = json.loads(line[:-2])
		id = page['id']
		try:
			title = page['labels']['pt-br']['value']
			text = page['descriptions']['pt-br']['value']
			tipo = "pt-br"
		except:
			try:
				title = page['labels']['en']['value']
				text = page['descriptions']['en']['value']	
				tipo = "en"
			except:
				title = ""

		if(title!=""):
			try:
				conn = psycopg2.connect("dbname='wiki' user='postgres' host='localhost' password='postgres' port='5432'")
				try:
					cur = conn.cursor()
					cur.execute("""INSERT INTO Artigo(id,titulo,texto,linguagem) VALUES(%s, %s, %s, %s)""", (id,title,text,tipo))
					conn.commit()
					cur.close()
					conn.close()
				except Exception as e:
					print ("Erro ao adicionar artigo")
					print(e)
			except Exception as e:
				print ("Erro de conexao com a database")
				print(e)			
file.close()



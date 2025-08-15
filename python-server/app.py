
#Import server modules
from flask import Flask, Response
from io import BytesIO

#import modules to load images from operative system
import os.path

#import necesary modules to draw in PDFs
#from reportlab.pdfgen.canvas import Canvas
from reportlab.lib.pagesizes import A4
from reportlab.platypus import Table, TableStyle, Image, BaseDocTemplate, Frame, PageBreak, NextPageTemplate, PageTemplate, FrameBreak, Spacer, Paragraph
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib import colors
from reportlab.lib.units import inch
from reportlab.lib.enums import TA_JUSTIFY, TA_CENTER, TA_LEFT, TA_RIGHT

#import module to open database
import mysql.connector

#To read env variables
from dotenv import load_dotenv

load_dotenv()
MYSQL_DATABASE = os.environ['MYSQL_DATABASE']
MYSQL_USER = os.environ['MYSQL_USER']
MYSQL_PASSWORD = os.environ['MYSQL_PASSWORD']

app = Flask(__name__)

@app.route('/')
def index():
    #Connectoin to db
    mydb = mysql.connector.connect(
        host='db',
        database=MYSQL_DATABASE,
        user=MYSQL_USER,
        password=MYSQL_PASSWORD,
        port=3306
    )
       
    # --------------------- Get all the foking data ---------------
    mycursor = mydb.cursor()
    mycursor.execute("SELECT nreloj, nombre, apellidoPaterno, apellidoMaterno FROM subject")
    data = mycursor.fetchall()

    #-------------------// PDF Document Creation //--------------------

    #file_name = 'lista_asistencia.pdf'
    document_title = 'Lista de Asistencia de Gremiado'
    title = 'Lista de Asistencia'
    username = '[username]'
    date = '04/07/25'

    #obtain the with and the h from the document to place objects
    w,h = int(A4[0]), int(A4[1])
    print(A4)

    #the instance of the pdf object is created, bottomup is true so location of the objects in
    #canvas is placed with the same coordinates as a web page
    #canvas = Canvas(file_name, bottomup=1, pagesize=A4)

    #----------------- Create in-memory buffer instead of a file -----------
    buffer = BytesIO()

    #------------ PLATYPUS CODE --------------

    doc = BaseDocTemplate(buffer, pagesize=A4)
    contents = []

    #----------- Marcos en el que se pondra contenido encima -------------
    left_header_frame = Frame(
        0.2*inch, 
        h-1.2*inch, 
        2*inch, 
        1*inch
        )

    right_header_frame = Frame(
        2.2*inch, 
        h-1.2*inch, 
        w-2.5*inch, 
        1*inch,id='normal'
        )

    frame_later = Frame(
        0.2*inch, 
        0.6*inch, 
        (w-0.6*inch)+0.17*inch, 
        h-1*inch,
        leftPadding = 0, 
        topPadding=0, 
        showBoundary = 1,
        id='col'
        )

    frame_table= Frame(
        0.2*inch, 
        0.7*inch, 
        (w-0.6*inch)+0.17*inch, 
        h-2*inch,
        leftPadding = 0, 
        topPadding=0, 
        showBoundary = 1,
        id='col'
        )

    #Aqui se utilizan los templates para definir como seran las proximas paginas en el documento
    laterpages = PageTemplate(id='laterpages', frames=[frame_later])
    firstpage = PageTemplate(id='firstpage', frames=[left_header_frame, right_header_frame, frame_table],)

    #Draw first page
    contents.append(NextPageTemplate('firstpage'))

    def path_relative_to_file(base_file_path, relative_path):
        base_dir = os.path.dirname(os.path.abspath(base_file_path))
        return os.path.normpath(os.path.join(base_dir, relative_path))

    #Draw STUC-CJ Logo
    #canvas.drawInlineImage(path_relative_to_file(__file__, './static/logo-sindicato.webp'), 50, h-73, 75, 75)
    logoleft = Image(path_relative_to_file(__file__, './static/logo-sindicato.webp'))
    logoleft._restrictSize(1.5*inch, 1.5*inch)
    logoleft.hAlign = 'CENTER'
    logoleft.vAlign = 'CENTER'

    #contents.append(logoleft)
    contents.append(FrameBreak())

    #Draw head information
    #canvas.setFont('Helvetica-Bold', 20)
    #canvas.drawCentredString(400, h-20, 'Lista de Asistencia')
    #canvas.setFont('Helvetica', 16)
    #canvas.drawCentredString(400, h-40, '[nombre de usuario]')
    #canvas.setFont('Helvetica', 14)
    #canvas.drawCentredString(400, h-55, '04/07/25')

    #Container for title
    styleSheet = getSampleStyleSheet()
    style_title = styleSheet['Heading1']
    style_title.fontSize = 20
    style_title.fontName = 'Helvetica-Bold'
    style_title.alignment = TA_CENTER

    #Container for username
    style_username = styleSheet['Normal']
    style_username.fontSize = 16
    style_username.fontName = 'Helvetica'
    style_title.alignment = TA_CENTER

    #Container for date
    style_date = styleSheet['Normal']
    style_date.fontSize = 14
    style_date.fontName = 'Helvetica'
    style_date.alignment = TA_CENTER


    #Add a title to the document
    #canvas.setTitle(document_title)

    #Draw all the stuff in the page
    contents.append(Paragraph(title, style_title))
    contents.append(Paragraph(username, style_username))
    contents.append(Paragraph(date, style_date))
    contents.append(FrameBreak())

    #n. reloj, apellidoP, apellidoM, nombres, firma
    headers = [('N. Reloj', 'Apellido Paterno', 'Apellido Materno', 'Nombre', 'Firma')]

    #It creates a table, and it adds the headers and al the data collected from the database
    datatable = Table((headers + data), colWidths=100, rowHeights=33)

    #Sets a color for the headers
    title_background = colors.fidblue
    #Table Styling
    tblStyle = TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), title_background),
        ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
        ('ALIGN', (1, 0), (1, -1), 'CENTER'),
        ('GRID', (0, 0), (-1, -1), 1, colors.black)
    ])

    #Sets a different color for an even number of cols
    rowNumb = len(data)
    for row in range(1, rowNumb):
        if row % 2 == 0:
            table_background = colors.lightblue
        else:
            table_background = colors.aliceblue

        tblStyle.add('BACKGROUND', (0, row), (-1, row), table_background)

    datatable.setStyle(tblStyle)

    #datatable.wrapOn(canvas, 150, 150)
    #datatable.drawOn(canvas, 25, 15)

    contents.append(NextPageTemplate('laterpages'))
    contents.append(datatable)

    contents.append(PageBreak())

    doc.addPageTemplates([firstpage, laterpages])
    doc.build(contents)

    #Prepare HTTP response
    pdf_value = buffer.getvalue()
    buffer.close()

    return Response(
        pdf_value,
        mimetype='application/pdf',
        headers={
            'Content-Disposition': 'attachment; filename="lista_asistencia.pdf"'
        }
    )


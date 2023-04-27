from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.firefox.options import Options


options = Options()
options.headless = True
#Classes
urlFromJava = "https://www.indeed.com/jobs?q=software&l=Greeley%2C+CO&from=searchOnHP&vjk=ee927b7df0eb76ee&advn=5738295507636633"
scrapedLocation = "css-6z8o9s:nth-child(2)"
scrapedTitle = "jobsearch-JobInfoHeader-title-container"
scrapedSalary = "css-gle1f1 > div:nth-child(1)"
#ID's
scrapedDescription = "jobDescriptionText"
salary = "salaryInfoAndJobType"


driver = webdriver.Firefox(options = options)
driver.get(urlFromJava)
elem = driver.find_element(By.CLASS_NAME,scrapedSalary)
print(elem.text)


# Rather than trying to scrape specific job applications provide lists of similar jobs.
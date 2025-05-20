import "./App.css";
import { getDocument, GlobalWorkerOptions, PDFWorker } from "pdfjs-dist";
import pdfWorker from "pdfjs-dist/build/pdf.worker.mjs?url";
import pdf from "pdf-parse";

function App() {
  GlobalWorkerOptions.workerSrc = pdfWorker;

  const extractTextFromPdf = async (file) => {
    try {
      const pdf = await getDocument(file).promise;
      const numPages = pdf.numPages;
      let fullText = "";

      for (let pageNum = 1; pageNum <= numPages; pageNum++) {
        const page = await pdf.getPage(pageNum);
        const content = await page.getTextContent();
        const pageText = content.items
          .map((item) => (item as any).str)
          .join(" ");
        fullText += pageText;
      }

      console.log(fullText);
    } catch (e) {
      console.log(e);
    }
  };

  const extractTextFromPdf2 = async (file) => {
    const text = await pdf(file);
  };

  const handleFile = (event) => {
    const file = URL.createObjectURL(event?.target.files[0]);
    extractTextFromPdf(file);
  };

  return (
    <main>
      <section>
        <h1>Finance App</h1>
      </section>
      <section className="bg-black">
        <input type="file" onChange={handleFile} />
      </section>
    </main>
  );
}

export default App;

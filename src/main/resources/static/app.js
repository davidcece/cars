$(".date").datepick({dateFormat: 'yyyy,dd,mm'});

const filterForm = $("#filterForm");

filterForm.submit(async function (e) {
    e.preventDefault();

    const display = $("#display").val();
    const output = $("#output");
    let response, cars;

    switch (display) {
        case "table":
            response = await getCars("api/json");
            cars = await response.json();
            output.html(getTable(cars))
            break;
        case "json":
            response = await getCars("api/json");
            cars = await response.json();
            output.text(JSON.stringify(cars, null, 2));
            break;
        case "xml":
            response = await getCars("api/xml");
            cars = await response.text();
            output.text(prettifyXml(cars));
            break;
    }
})


async function getCars(url){
    const data = filterForm.serializeArray();
    if (data.length > 0) {
        url += "?";
        data.forEach((item, index) => {
            url += `${item.name}=${item.value}`;
            if (index < data.length - 1) {
                url += "&";
            }
        });
    }

    return await fetch(url);
}


function getTable(cars) {
    let table = `
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Brand</th>
            <th>Model</th>
            <th>Type</th>
            <th>Release Date</th>
            <th>Price USD</th>
            <th>Price EUR</th>
            <th>Price GBP</th>
            <th>Price JPY</th>
        </tr>
        </thead>
        <tbody>`;

    cars.forEach((car) => {
        table += `
            <tr>
                <td>${car.brand}</td>
                <td>${car.model}</td>
                <td>${car.type}</td>
                <td>${car.releaseDate}</td>
                <td>${car.priceUSD}</td>
                <td>${car.priceEUR}</td>
                <td>${car.priceGBP}</td>
                <td>${car.priceJPY}</td>
            </tr>`
    });

    table += ` 
        </tbody>
     </table>`;

    return table;
}


function prettifyXml(sourceXml)
{
    const xmlDoc = new DOMParser().parseFromString(sourceXml, 'application/xml');
    const xsltDoc = new DOMParser().parseFromString([
        // describes how we want to modify the XML - indent everything
        '<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">',
        '  <xsl:strip-space elements="*"/>',
        '  <xsl:template match="para[content-style][not(text())]">', // change to just text() to strip space in text nodes
        '    <xsl:value-of select="normalize-space(.)"/>',
        '  </xsl:template>',
        '  <xsl:template match="node()|@*">',
        '    <xsl:copy><xsl:apply-templates select="node()|@*"/></xsl:copy>',
        '  </xsl:template>',
        '  <xsl:output indent="yes"/>',
        '</xsl:stylesheet>',
    ].join('\n'), 'application/xml');

    const xsltProcessor = new XSLTProcessor();
    xsltProcessor.importStylesheet(xsltDoc);
    const resultDoc = xsltProcessor.transformToDocument(xmlDoc);
    return new XMLSerializer().serializeToString(resultDoc);
}

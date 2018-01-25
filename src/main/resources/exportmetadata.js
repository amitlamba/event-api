var finalresults = {}

db.events.aggregate([
    {
        $project: {
            name: "$name",
            attributes: "$attributes"
        }
    }
]).forEach(function (doc) {
    if (!finalresult[
            doc.name
            ]) {
        finalresult[
            doc.name
            ] = {
            name: doc.name,
            properties: {}
        };
    }
    result = finalresult[doc.name];


    Object.keys(doc.attributes).forEach(function (key) {
        if (!result.properties[key]) {
            result.properties[key] = {name: key, value: []};
        }

        if (!Array.isArray(doc.attributes[key])) {
            var exists = contains.call(result.properties[key].value, doc.attributes[key]);

            if (!exists) {
                result.properties[key].value.push(doc.attributes[key]);
            }
        }
        if (Array.isArray(doc.attributes[key])) {
            doc.attributes[key].forEach(function (ival) {
                var exists = contains.call(result.properties[key].value, ival);

                if (!exists) {
                    result.properties[key].value.push(ival);
                }
            })
        }
    });
})
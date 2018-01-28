
var computemetadata = function (collection) {
    var contains = function (needle) {
        // Per spec, the way to identify NaN is that it is not equal to itself
        var findNaN = needle !== needle;
        var indexOf;

        if (!findNaN && typeof Array.prototype.indexOf === 'function') {
            indexOf = Array.prototype.indexOf;
        } else {
            indexOf = function (needle) {
                var i = -1, index = -1;

                for (i = 0; i < this.length; i++) {
                    var item = this[i];

                    if ((findNaN && item !== item) || item === needle) {
                        index = i;
                        break;
                    }
                }

                return index;
            };
        }

        return indexOf.call(this, needle) > -1;
    };


    var finalresult = {};

    db[collection].aggregate([{$project: {name: "$name", attributes: "$attributes"}}]).forEach(function (doc) {

        if (!finalresult[doc.name]) {
            finalresult[doc.name] = {name: doc.name, properties: {}};
        }
        var result = finalresult[doc.name];


        Object.keys(doc.attributes).forEach(function (key) {

            if (!result.properties[key]) {
                result.properties[key] = {name: key, options: []};
            }

            if (!Array.isArray(doc.attributes[key])) {
                var exists = contains.call(result.properties[key].options, doc.attributes[key]);

                if (!exists) {
                    result.properties[key].options.push(doc.attributes[key]);
                }
            }
            if (Array.isArray(doc.attributes[key])) {

                doc.attributes[key].forEach(function (ival) {
                    var exists = contains.call(result.properties[key].options, ival);

                    if (!exists) {
                        result.properties[key].options.push(ival);
                    }
                })
            }

        });

    });
    Object.keys(finalresult).forEach(function (key) {
        var collectionmetadata = collection+"metadata";
        var metadata = {"name":key,  properties:[]};
        var properties = []
        Object.keys(finalresult[key].properties).forEach(function (value) {
            properties.push({"name":value, options:finalresult[key].properties[value].options})
        });
        metadata.properties = properties;
        db[collectionmetadata].insert(metadata);
    })
    //db.metadata.insert(finalresult);
    return finalresult;
}

var x = computemetadata("2_event");
x;

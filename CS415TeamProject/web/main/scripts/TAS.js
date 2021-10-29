var TAS =  ( function() {

    var employeeid = null;
    var payperiod = null;

    return {
        
        init: function() {
            
            employeeid = $("#employeeid").val();
            payperiod = $("#payperiod").val();
            
            this.getPayPeriodPunchList();
            
        },
        
        getPayPeriodPunchList: function() {
            
            var that = this;
            
            var parameters = {};
            
            parameters["employeeid"] = employeeid;
            parameters["payperiod"] = payperiod;
            
            $.ajax({
                
                url: 'PunchList',
                method: 'GET',
                data: parameters,
                dataType: 'json',
                
                success: function(json) {
                    
                    that.punchListToTable(json);

                }

            });
            
        },
        
        punchListToTable: function(json) {
            
            var punch_table = document.createElement("table");
            punch_table.setAttribute("id", "punchtable");
            punch_table.setAttribute("style", "border: 1px solid black;");
            
            var punch_keys = ["id", "terminalid", "punchtype", "originaltimestamp", "adjustedtimestamp", "adjustmenttype"];
            
            var punch_table_header_row = document.createElement("tr");
            
            for (var key in punch_keys) {
            
                var punch_table_header_col = document.createElement("th");
                punch_table_header_col.setAttribute("style", "border: 1px solid black;");
                punch_table_header_col.innerHTML = punch_keys[key];
                punch_table_header_row.appendChild(punch_table_header_col);
                
            }
            
            punch_table.appendChild(punch_table_header_row);

            for (var element in json["punchlist"]) {
                
                var punch_table_data_row = document.createElement("tr");

                var punch = json["punchlist"][element];
                
                for (var key in punch_keys) {
                    
                    var value = punch[punch_keys[key]];

                    var punch_table_data_col = document.createElement("td");
                    punch_table_data_col.setAttribute("style", "border: 1px solid black;");
                    
                    if (punch_keys[key] === "id")
                        punch_table_data_col.innerHTML = "<input type=\"radio\" name=\"punchid\" value=\"" + value + "\">" + value;
                    else
                        punch_table_data_col.innerHTML = value;
                    
                    punch_table_data_row.appendChild(punch_table_data_col);
                    
                }

                punch_table.appendChild(punch_table_data_row);

            }

            $("#punchtable_target").html(punch_table);
            
            $("#totalminutes_target").html(json["totalminutes"]);
            
            $("#totalabsenteeism_target").html(json["absenteeism"]);
            
        },
        
        onClickDelete: function() {
            
            var punchid = $("#punchedit input[name=punchid]:checked").val();
            $("#punchedit input[name=deletepunchid]").val(punchid);
            
        }
        
    };

})();
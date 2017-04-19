setmetatable(_G, {
    __index = function(self, key)
        local className = string.gsub(key, '%_%_', '$')
        className = string.gsub(className, '%_', '.')

        local class = luajava.bindClass(className)
        if class then
            self[key] = class
        end

        return class
    end,
})

-- require "wax.ext"
-- require "wax.enums"
-- require "wax.structs"
require "wax.waxClass"
-- require "wax.helpers"

new = luajava.new

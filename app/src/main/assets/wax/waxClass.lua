function waxClass(options)
    local className = options[1]
    local superClass = options[2]
    local protocols = options.protocols

    local class = { name = className }

    if superClass then
        setmetatable(class, { __index = superClass })
        class.super = superClass
    end

    if protocols and #protocols > 0 then
        protocols = table.concat(protocols, ',')
        class.listener = luajava.createProxy(protocols, class)
    end

    local _M = setmetatable({ self = class }, {
        __newindex = function(self, key, value)
            class[key] = value
        end,

        __index = function(self, key)
            return class[key] or _G[key]
        end,
    })

    _G[className] = class
    package.loaded[className] = class
    setfenv(2, _M)

    return class
end

function waxListener(protocols, class)
    if protocols and #protocols > 0 then
        protocols = table.concat(protocols, ',')
        return luajava.createProxy(protocols, class)
    end

    return false
end

import json
import sys

class HighPerformanceEventCorrelationSystem:
    def __init__(self, json_data):
        self.data = json.loads(json_data)

    def get(self, keys):
        try:
            value = self.data
            for key in keys:
                if isinstance(value, list):
                    key = int(key)
                value = value[key]
            print(json.dumps(value))
        except KeyError:
            print(f"GET_KEY_NOT_FOUND \"{key}\"")
        except IndexError:
            print(f"GET_ERROR_INDEX_OUT_OF_RANGE \"{key}\"")
        except (ValueError, TypeError):
            if isinstance(value, list):
                print(f"GET_ERROR_INDEX_OUT_OF_RANGE \"{key}\"")
            else:
                print(f"GET_ERROR_NOT_ARRAY \"{key}\"")

    def put(self, keys, value):
        try:
            target = self.data
            for key in keys[:-1]:
                if isinstance(target, list):
                    key = int(key)
                target = target[key]
            last_key = keys[-1]
            if isinstance(target, list):
                target[int(last_key)] = value
            else:
                target[last_key] = value
        except KeyError:
            print(f"PUT_KEY_NOT_FOUND \"{key}\"")
        except IndexError:
            print(f"PUT_ERROR_INDEX_OUT_OF_RANGE \"{key}\"")
        except (ValueError, TypeError):
            if isinstance(target, list):
                print(f"PUT_ERROR_INDEX_OUT_OF_RANGE \"{key}\"")
            else:
                print(f"PUT_ERROR_NOT_OBJECT \"{key}\"")

    def delete(self, keys):
        try:
            target = self.data
            for key in keys[:-1]:
                if isinstance(target, list):
                    key = int(key)
                target = target[key]
            last_key = keys[-1]
            if isinstance(target, list):
                target.pop(int(last_key))
            else:
                del target[last_key]
        except KeyError:
            print(f"DEL_KEY_NOT_FOUND \"{key}\"")
        except IndexError:
            print(f"DEL_ERROR_INDEX_OUT_OF_RANGE \"{key}\"")
        except (ValueError, TypeError):
            if isinstance(target, list):
                print(f"DEL_ERROR_INDEX_OUT_OF_RANGE \"{key}\"")
            else:
                print(f"DEL_ERROR_NOT_OBJECT \"{key}\"")

def main(json_file, input_file, output_file):
    with open(json_file, 'r') as jf:
        json_data = jf.read()
    with open(input_file, 'r') as inf:
        commands = inf.readlines()
    
    hpecs = HighPerformanceEventCorrelationSystem(json_data)
    
    original_stdout = sys.stdout
    with open(output_file, 'w') as of:
        sys.stdout = of
        for command in commands:
            parts = command.strip().split()
            if not parts:
                continue
            op = parts[0]
            if op == "GET":
                hpecs.get(parts[1:])
            elif op == "PUT":
                hpecs.put(parts[1:-1], json.loads(parts[-1]))
            elif op == "DEL":
                hpecs.delete(parts[1:])
        sys.stdout = original_stdout

if __name__ == "__main__":
    main(sys.argv[1], sys.argv[2], sys.argv[3])
